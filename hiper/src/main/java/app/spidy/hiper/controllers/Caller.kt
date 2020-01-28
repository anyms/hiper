package app.spidy.hiper.controllers

import okhttp3.Call

class Caller(private val call: Call) {
    val isCanceled: Boolean
        get() = call.isCanceled()
    val isExecuted: Boolean
        get() = call.isExecuted()

    fun cancel() {
        call.cancel()
    }
}