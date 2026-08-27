// port-lint: source rama-tls-rustls/src/key_log.rs
package io.github.kotlinmania.ramatlsrustls

/**
 * PlainHex wrapper for hexadecimal formatting of byte slices.
 */
class PlainHex(
    val slice: ByteArray,
) {
    fun fmt(): String = slice.toLowerHex()
    fun fmtInnerHex(): String = slice.toLowerHex()
    override fun toString(): String = fmt()
}

/**
 * KeyLog interface for TLS secret key logging.
 */
interface KeyLog {
    /**
     * Log a keylog entry for the given label, client random bytes, and secret bytes.
     */
    fun log(label: String, clientRandom: ByteArray, secret: ByteArray)
}

/**
 * Handle to an underlying keylog file sink.
 */
class KeyLogFileHandle(
    val path: String,
) {
    private val buffer = StringBuilder()

    /**
     * Writes a single log line to the keylog file.
     */
    fun writeLogLine(line: String) {
        buffer.append(line)
    }

    /**
     * Returns the accumulated log content.
     */
    fun content(): String = buffer.toString()
}

/**
 * Tries to create a new key log file handle for the given file path.
 */
fun tryNewKeyLogFileHandle(path: String): KeyLogFileHandle = KeyLogFileHandle(path)

/**
 * [KeyLog] implementation that opens a file for the given path.
 */
class KeyLogFile(
    val handle: KeyLogFileHandle,
) : KeyLog {

    companion object {
        /**
         * Makes a new [KeyLogFile] from the given file path.
         */
        fun tryNew(path: String): KeyLogFile {
            val handle = tryNewKeyLogFileHandle(path)
            return KeyLogFile(handle)
        }
    }

    override fun log(label: String, clientRandom: ByteArray, secret: ByteArray) {
        val randomHex = PlainHex(clientRandom).fmt()
        val secretHex = PlainHex(secret).fmt()
        val line = "$label $randomHex $secretHex\n"
        handle.writeLogLine(line)
    }
}

private fun ByteArray.toLowerHex(): String {
    val chars = CharArray(size * 2)
    for (i in indices) {
        val v = this[i].toInt() and 0xFF
        val high = v ushr 4
        val low = v and 0x0F
        chars[i * 2] = if (high < 10) ('0'.code + high).toChar() else ('a'.code + (high - 10)).toChar()
        chars[i * 2 + 1] = if (low < 10) ('0'.code + low).toChar() else ('a'.code + (low - 10)).toChar()
    }
    return chars.concatToString()
}
