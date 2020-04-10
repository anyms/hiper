package app.spidy.hiper

import app.spidy.hiper.controllers.Caller
import app.spidy.hiper.data.HiperResponse
import app.spidy.hiper.interfaces.Listener
import okhttp3.*
import java.io.File


/* Hiper */

class Hiper {
    fun get(
        url: String, isStream: Boolean = false, byteSize: Int = 4096,
        args: HashMap<String, Any> = hashMapOf(),
        headers: HashMap<String, Any> = hashMapOf(),
        cookies: HashMap<String, String> = hashMapOf(),
        username: String? = null,
        password: String? = null,
        timeout: Long? = null
    ): HiperResponse {
        return GetRequest(url, isStream, byteSize, args=args, headers=headers, cookies=cookies,
            username = username, password = password, timeout=timeout).sync()
    }

    fun post(
        url: String, isStream: Boolean = false, byteSize: Int = 4096,
        args: HashMap<String, Any> = hashMapOf(),
        form: HashMap<String, Any> = hashMapOf(),
        files: List<File> = listOf(),
        headers: HashMap<String, Any> = hashMapOf(),
        cookies: HashMap<String, String> = hashMapOf(),
        username: String? = null,
        password: String? = null,
        timeout: Long? = null
    ): HiperResponse {
        return PostRequest(url, isStream, byteSize, args=args, form=form, files=files, headers=headers,
            cookies=cookies, username=username, password = password, timeout = timeout).sync()
    }

    fun head(
        url: String, isStream: Boolean = false, byteSize: Int = 4096,
        args: HashMap<String, Any> = hashMapOf(),
        headers: HashMap<String, Any> = hashMapOf(),
        cookies: HashMap<String, String> = hashMapOf(),
        username: String? = null,
        password: String? = null,
        timeout: Long? = null
    ): HiperResponse {
        return HeadRequest(url, isStream, byteSize, args=args, headers=headers, cookies=cookies,
            username = username, password = password, timeout=timeout).sync()
    }


    inner class Async {
        fun get(
            url: String, isStream: Boolean = false, byteSize: Int = 4096,
            args: HashMap<String, Any> = hashMapOf(),
            headers: HashMap<String, Any> = hashMapOf(),
            cookies: HashMap<String, String> = hashMapOf(),
            username: String? = null,
            password: String? = null,
            timeout: Long? = null
        ): GetRequest.Queue {
            return GetRequest(url, isStream, byteSize, args=args, headers=headers, cookies=cookies,
                username = username, password = password, timeout=timeout).Queue()
        }

        fun post(
            url: String, isStream: Boolean = false, byteSize: Int = 4096,
            args: HashMap<String, Any> = hashMapOf(),
            form: HashMap<String, Any> = hashMapOf(),
            files: List<File> = listOf(),
            headers: HashMap<String, Any> = hashMapOf(),
            cookies: HashMap<String, String> = hashMapOf(),
            username: String? = null,
            password: String? = null,
            timeout: Long? = null
        ): PostRequest.Queue {
            return PostRequest(url, isStream, byteSize, args=args, form=form, files=files,
                headers=headers, cookies=cookies, username=username, password=password, timeout = timeout).Queue()
        }

        fun head(
            url: String, isStream: Boolean = false, byteSize: Int = 4096,
            args: HashMap<String, Any> = hashMapOf(),
            headers: HashMap<String, Any> = hashMapOf(),
            cookies: HashMap<String, String> = hashMapOf(),
            username: String? = null,
            password: String? = null,
            timeout: Long? = null
        ): HeadRequest.Queue {
            return HeadRequest(url, isStream, byteSize, args=args, headers=headers, cookies=cookies,
                username = username, password = password, timeout=timeout).Queue()
        }
    }

    companion object {
        fun getAsyncInstance(): Hiper.Async {
            return Hiper().Async()
        }
        fun getSyncInstance(): Hiper {
            return Hiper()
        }
    }
}