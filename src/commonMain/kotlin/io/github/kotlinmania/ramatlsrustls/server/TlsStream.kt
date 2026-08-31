// port-lint: source rama-tls-rustls/src/server/tls_stream.rs
package io.github.kotlinmania.ramatlsrustls.server

/**
 * Server TLS stream wrapping an underlying IO stream.
 */
class TlsStream<IO>(
    val stream: IO,
) {
    fun getRef(): IO = stream

    fun getMut(): IO = stream

    fun extensions(): Any? = null

    fun extensionsMut(): Any? = null

    fun pollRead(buffer: ByteArray): Int = buffer.size

    fun pollWrite(bytes: ByteArray): Int = bytes.size

    fun pollWriteVectored(bufs: List<ByteArray>): Int = bufs.sumOf { it.size }

    fun pollFlush(): Boolean = true

    fun pollShutdown(): Boolean = true

    fun isWriteVectored(): Boolean = false

    companion object {
        fun <IO> new(stream: IO): TlsStream<IO> = TlsStream(stream)

        fun <IO> from(stream: IO): TlsStream<IO> = new(stream)
    }
}
