package app.spidy.hiper.data

data class Listener(
    var ifFailed: ((Response) -> Unit)? = null,
    var ifException: ((Exception) -> Unit)? = null,
    var ifStream: ((buffer: ByteArray?, byteSize: Int) -> Unit)? = null,
    var ifFailedOrException: (() -> Unit)? = null
)