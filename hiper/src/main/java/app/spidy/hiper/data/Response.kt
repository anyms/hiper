package app.spidy.hiper.data

data class Response(
    val isRedirect: Boolean,
    val statusCode: Int,
    val message: String,
    var text: String? = null,
    var content: ByteArray? = null,
    val headers: Headers,
    var isSuccessful: Boolean = false
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Response

        if (isRedirect != other.isRedirect) return false
        if (statusCode != other.statusCode) return false
        if (message != other.message) return false
        if (text != other.text) return false
        if (content != null) {
            if (other.content == null) return false
            if (!content!!.contentEquals(other.content!!)) return false
        } else if (other.content != null) return false
        if (headers != other.headers) return false
        if (isSuccessful != other.isSuccessful) return false

        return true
    }

    override fun hashCode(): Int {
        var result = isRedirect.hashCode()
        result = 31 * result + statusCode
        result = 31 * result + message.hashCode()
        result = 31 * result + (text?.hashCode() ?: 0)
        result = 31 * result + (content?.contentHashCode() ?: 0)
        result = 31 * result + headers.hashCode()
        result = 31 * result + isSuccessful.hashCode()
        return result
    }
}