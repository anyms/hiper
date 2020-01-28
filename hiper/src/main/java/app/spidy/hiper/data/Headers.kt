package app.spidy.hiper.data

import java.util.*
import kotlin.collections.HashMap

class Headers {
    private val headers = HashMap<String, String>()

    override fun toString(): String {
        return headers.toString()
    }

    fun toHashMap(): HashMap<String, String> {
        return  headers
    }

    fun get(key: String): String? {
        return headers[key.toLowerCase(Locale.getDefault())]
    }

    fun put(key: String, value: String) {
        headers[key.toLowerCase(Locale.getDefault())] = value
    }
}