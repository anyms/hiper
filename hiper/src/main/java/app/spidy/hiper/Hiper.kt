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
        password: String? = null
    ): HiperResponse {
        return GetRequest(url, isStream, byteSize, args=args, headers=headers, cookies=cookies,
            username = username, password = password).sync()
    }

    fun post(
        url: String, isStream: Boolean = false, byteSize: Int = 4096,
        args: HashMap<String, Any> = hashMapOf(),
        form: HashMap<String, Any> = hashMapOf(),
        files: List<File> = listOf(),
        headers: HashMap<String, Any> = hashMapOf(),
        cookies: HashMap<String, String> = hashMapOf(),
        auths: HashMap<String, Any>,
        username: String? = null,
        password: String? = null
    ): HiperResponse {
        return PostRequest(url, isStream, byteSize, args=args, form=form, files=files, headers=headers,
            cookies=cookies, username=username, password = password).sync()
    }

    fun head(
        url: String, isStream: Boolean = false, byteSize: Int = 4096,
        args: HashMap<String, Any> = hashMapOf(),
        headers: HashMap<String, Any> = hashMapOf(),
        cookies: HashMap<String, String> = hashMapOf(),
        username: String? = null,
        password: String? = null
    ): HiperResponse {
        return HeadRequest(url, isStream, byteSize, args=args, headers=headers, cookies=cookies,
            username = username, password = password).sync()
    }


    inner class Async {
        fun get(
            url: String, isStream: Boolean = false, byteSize: Int = 4096,
            args: HashMap<String, Any> = hashMapOf(),
            headers: HashMap<String, Any> = hashMapOf(),
            cookies: HashMap<String, String> = hashMapOf(),
            username: String? = null,
            password: String? = null
        ): GetRequest.Queue {
            return GetRequest(url, isStream, byteSize, args=args, headers=headers, cookies=cookies,
                username = username, password = password).Queue()
        }

        fun post(
            url: String, isStream: Boolean = false, byteSize: Int = 4096,
            args: HashMap<String, Any> = hashMapOf(),
            form: HashMap<String, Any> = hashMapOf(),
            files: List<File> = listOf(),
            headers: HashMap<String, Any> = hashMapOf(),
            cookies: HashMap<String, String> = hashMapOf(),
            username: String? = null,
            password: String? = null
        ): PostRequest.Queue {
            return PostRequest(url, isStream, byteSize, args=args, form=form, files=files,
                headers=headers, cookies=cookies, username=username, password=password).Queue()
        }

        fun head(
            url: String, isStream: Boolean = false, byteSize: Int = 4096,
            args: HashMap<String, Any> = hashMapOf(),
            headers: HashMap<String, Any> = hashMapOf(),
            cookies: HashMap<String, String> = hashMapOf(),
            username: String? = null,
            password: String? = null
        ): HeadRequest.Queue {
            return HeadRequest(url, isStream, byteSize, args=args, headers=headers, cookies=cookies,
                username = username, password = password).Queue()
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