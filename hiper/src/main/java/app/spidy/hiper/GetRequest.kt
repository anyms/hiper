package app.spidy.hiper

import app.spidy.hiper.controllers.Caller
import app.spidy.hiper.data.Headers
import app.spidy.hiper.data.HiperResponse
import app.spidy.hiper.interfaces.Listener
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import org.riversun.okhttp3.OkHttp3CookieHelper
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream

class GetRequest(
    private val url: String,
    private val isStream: Boolean,
    private val byteSize: Int,
    private val args: HashMap<String, Any>,
    private val headers: HashMap<String, Any>,
    private val cookies: HashMap<String, String>,
    private val username: String?,
    private val password: String?
) {
    private lateinit var client: OkHttpClient

    private fun build(): Request {
        val urlBuilder = url.toHttpUrlOrNull()?.newBuilder() ?: throw Exception("UrlBuilder returns null")
        val cookieManager = OkHttp3CookieHelper()
        val request = Request.Builder()

        for ((k, v) in headers) request.addHeader(k, v.toString())
        for ((k, v) in args) urlBuilder.addQueryParameter(k, v.toString())
        if (username != null && password != null) {
            request.addHeader("Authorization", Credentials.basic(username, password))
        }

        val u = urlBuilder.build().toString()
        for ((k, v) in cookies) cookieManager.setCookie(u, k, v)
        client = OkHttpClient.Builder()
            .cookieJar(cookieManager.cookieJar())
            .build()
        return request.url(urlBuilder.build().toString()).get().build()
    }

    fun sync(): HiperResponse {
        val request = build()
        val response = client.newCall(request).execute()
        val stream = response.body?.byteStream()
        var bytes: ByteArray? = null
        var text: String? = null
        val headers = Headers()

        if (!isStream) {
            bytes = readBytes(stream)
            text = String(bytes)
        }

        for ((k, v) in response.headers) headers.put(k, v)

        return HiperResponse(
            isRedirect = response.isRedirect,
            statusCode = response.code,
            message = response.message,
            text = text,
            content = bytes,
            stream = stream,
            headers = headers,
            isSuccessful = response.isSuccessful
        )
    }

    fun async(listener: Listener): Caller {
        val request = build()
        val call = client.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                listener.onFail(e)
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    val stream = response.body?.byteStream()
                    var bytes: ByteArray? = null
                    var text: String? = null
                    val headers = Headers()

                    if (!isStream) {
                        bytes = readBytes(stream)
                        text = String(bytes)
                    }

                    for ((k, v) in response.headers) headers.put(k, v)

                    val hiperResponse = HiperResponse(
                        isRedirect = response.isRedirect,
                        statusCode = response.code,
                        message = response.message,
                        text = text,
                        content = bytes,
                        stream = stream,
                        headers = headers,
                        isSuccessful = response.isSuccessful
                    )
                    listener.onSuccess(hiperResponse)
                } catch (e: Exception) {
                    listener.onFail(e)
                }
            }
        })

        return Caller(call)
    }

    private fun readBytes(inputStream: InputStream?): ByteArray {
        val byteBuffer = ByteArrayOutputStream()
        val buffer = ByteArray(byteSize)
        var len: Int
        while (true) {
            if (inputStream == null) break
            len = inputStream.read(buffer)
            if (len == -1) break
            byteBuffer.write(buffer, 0, len)
        }
        return byteBuffer.toByteArray()
    }


    inner class Queue {
        private lateinit var thenCallback: (hiperResponse: HiperResponse) -> Unit

        fun then(callback: (hiperResponse: HiperResponse) -> Unit): Queue {
            thenCallback = callback
            return this
        }
        fun catch(callback: ((Exception) -> Unit)? = null): Caller {
            return async(object : Listener {
                override fun onFail(e: Exception) {
                    callback?.invoke(e)
                }

                override fun onSuccess(response: HiperResponse) {
                    thenCallback(response)
                }
            })
        }
    }
}