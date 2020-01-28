package app.spidy.hiper.controllers

import app.spidy.hiper.BuildConfig
import app.spidy.hiper.data.Headers
import app.spidy.hiper.data.Listener
import app.spidy.hiper.data.Params
import app.spidy.hiper.data.Response
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import java.io.IOException
import java.io.InputStream
import kotlin.Exception
import kotlin.concurrent.thread


/* Hiper */

class Hiper {
    private val client = OkHttpClient()

    fun get(url: String, isStream: Boolean = false, byteSize: Int = 4096): HttpHandler {
        return HttpHandler(url, isStream, byteSize, action="get")
    }

    fun post(url: String, isStream: Boolean = false, byteSize: Int = 4096): HttpHandler {
        return HttpHandler(url, isStream, byteSize, action="post")
    }

    fun head(url: String, isStream: Boolean = false, byteSize: Int = 4096): HttpHandler {
        return HttpHandler(url, isStream, byteSize, action="head")
    }

    fun put(url: String, isStream: Boolean = false, byteSize: Int = 4096): HttpHandler {
        return HttpHandler(url, isStream, byteSize, action="put")
    }


    inner class HttpHandler(
        private val url: String,
        private val isStream: Boolean,
        private val byteSize: Int,
        private val action: String
    ) {
        private val listener = Listener()
        private val formData = HashMap<String, String>()
        private val headers = Headers()
        private val args = HashMap<String, String>()

        init {
            headers.put("User-Agent", "Fetcher/" + BuildConfig.VERSION_NAME)
        }

        fun addHeader(key: String, value: Any?): HttpHandler {
            headers.put(key, value.toString())
            return this
        }

        fun addFormData(key: String, value: Any?): HttpHandler {
            formData[key] = value.toString()
            return this
        }

        fun addFormData(args: HashMap<String, Any?>): HttpHandler {
            for ((key, value) in args) {
                this.formData[key] = value.toString()
            }
            return this
        }

        fun addArg(key: String, value: Any?): HttpHandler {
            args[key] = value.toString()
            return this
        }

        fun addArgs(args: HashMap<String, Any?>): HttpHandler {
            for ((key, value) in args) {
                this.args[key] = value.toString()
            }
            return this
        }

        fun ifFailed(callback: (Response) -> Unit): HttpHandler {
            listener.ifFailed = callback
            return this
        }

        fun finally(callback: (Response) -> Unit): Caller {
            var request: Request? = null

            /* Build the appropriate requests */
            when (action) {
                "get" -> {
                    val urlBuilder = url.toHttpUrlOrNull()?.newBuilder() ?: throw Exception("UrlBuilder return null")
                    request = __get_request(urlBuilder, args, headers.toHashMap())
                }
                "post" -> {
                    val urlBuilder = url.toHttpUrlOrNull()?.newBuilder() ?: throw Exception("UrlBuilder return null")
                    request = __post_request(urlBuilder, args, formData, headers.toHashMap())
                }
                "head" -> {
                    val urlBuilder = url.toHttpUrlOrNull()?.newBuilder() ?: throw Exception("UrlBuilder return null")
                    request = __head_request(urlBuilder, args, headers.toHashMap())
                }
                "put" -> {
                    val urlBuilder = url.toHttpUrlOrNull()?.newBuilder() ?: throw Exception("UrlBuilder return null")
                    request = __put_request(urlBuilder, args, headers.toHashMap())
                }
            }

            val call = client.newCall(request!!)
            thread {
                try {
                    val response = call.execute()
                    val respHeaders = Headers()
                    for ((key, value) in response.headers) {
                        respHeaders.put(key, value)
                    }
                    val resp = Response(
                        response.isRedirect,
                        response.code,
                        response.message,
                        null,
                        null,
                        respHeaders
                    )
                    if (isStream && response.isSuccessful) {
                        var inputStream: InputStream? = null
                        try {
                            inputStream = response.body?.byteStream()
                            val buffer = ByteArray(byteSize)
                            while (true) {
                                val bytes = inputStream?.read(buffer)
                                if (bytes == -1 || bytes == null) {
                                    break
                                }
                                listener.ifStream?.invoke(buffer, bytes)
                            }
                        } catch (e: Exception) {
                            listener.ifException?.invoke(e)
                            listener.ifFailedOrException?.invoke()
                        } finally {
                            inputStream?.close()
                            listener.ifStream?.invoke(null, -1)
                        }
                    } else {
                        val content = response.body?.bytes()
                        resp.text = if (content == null) null else String(content)
                        resp.content = content
                    }
                    if (response.isSuccessful) {
                        callback.invoke(resp)
                    } else {
                        listener.ifFailed?.invoke(resp)
                    }
                } catch (e: Exception) {
                    listener.ifException?.invoke(e)
                    listener.ifFailedOrException?.invoke()
                }
            }

            return Caller(call)
        }

        fun ifException(callback: (Exception?) -> Unit): HttpHandler {
            listener.ifException = callback
            return this
        }

        fun ifStream(callback: (buffer: ByteArray?, byteSize: Int) -> Unit): HttpHandler {
            listener.ifStream = callback
            return this
        }

        fun ifFailedOrException(callback: () -> Unit): HttpHandler {
            listener.ifFailedOrException = callback
            return this
        }



        private fun __get_request(
            urlBuilder: HttpUrl.Builder,
            args: HashMap<String, String>, headers: HashMap<String, String>): Request {
            val request = Request.Builder()

            for ((key, value) in headers) {
                request.addHeader(key, value)
            }
            for ((key, value) in args) {
                urlBuilder.addQueryParameter(key, value)
            }
            return request.url(urlBuilder.build().toString()).get().build()
        }

        private fun __post_request(
            urlBuilder: HttpUrl.Builder, args: HashMap<String, String>,
            formData: HashMap<String, String>, headers: HashMap<String, String>): Request {
            if (formData.isEmpty()) {
                formData["__hiper"] = BuildConfig.VERSION_NAME
            }

            val requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
            val request = Request.Builder()

            for ((key, value) in formData) {
                requestBody.addFormDataPart(key, value)
            }
            for ((key, value) in headers) {
                request.addHeader(key, value)
            }
            for ((key, value) in args) {
                urlBuilder.addQueryParameter(key, value)
            }

            return request.url(urlBuilder.build().toString()).post(requestBody.build()).build()
        }

        private fun __head_request(urlBuilder: HttpUrl.Builder, args: HashMap<String, String>, headers: HashMap<String, String>): Request {
            val request = Request.Builder()

            for ((key, value) in headers) {
                request.addHeader(key, value)
            }
            for ((key, value) in args) {
                urlBuilder.addQueryParameter(key, value)
            }
            return request.url(urlBuilder.build().toString()).head().build()
        }

        private fun __put_request(urlBuilder: HttpUrl.Builder, args: HashMap<String, String>, headers: HashMap<String, String>): Request {
            if (args.isEmpty()) {
                args["__hiper"] = BuildConfig.VERSION_NAME
            }

            val requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
            val request = Request.Builder()

            for ((key, value) in args) {
                requestBody.addFormDataPart(key, value)
            }
            for ((key, value) in headers) {
                request.addHeader(key, value)
            }
            for ((key, value) in args) {
                urlBuilder.addQueryParameter(key, value)
            }

            return request.url(urlBuilder.build().toString()).put(requestBody.build()).build()
        }
    }
}