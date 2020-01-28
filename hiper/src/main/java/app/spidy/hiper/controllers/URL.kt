package app.spidy.hiper.controllers

import java.net.URLDecoder
import java.net.URLEncoder

object URL {
    fun encode(url: String): String = URLEncoder.encode(url, "UTF-8")
    fun decode(url: String): String = URLDecoder.decode(url, "UTF-8")
}