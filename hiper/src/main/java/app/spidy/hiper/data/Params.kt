package app.spidy.hiper.data

import java.util.*
import kotlin.collections.HashMap

class Params {
    private val params = HashMap<String, String>()

    override fun toString(): String {
        return params.toString()
    }

    fun toHashMap(): HashMap<String, String> {
        return params
    }

    fun get(key: String): String? {
        return params[key.toLowerCase(Locale.getDefault())]
    }

    fun put(key: String, value: String) {
        params[key.toLowerCase(Locale.getDefault())] = value
    }
}