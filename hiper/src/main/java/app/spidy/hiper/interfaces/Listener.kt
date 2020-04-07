package app.spidy.hiper.interfaces

import app.spidy.hiper.data.HiperResponse

interface Listener {
    fun onSuccess(response: HiperResponse)
    fun onFail(e: Exception)
}