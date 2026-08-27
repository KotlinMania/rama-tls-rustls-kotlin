// port-lint: source client/tls_stream_auto.rs
package io.github.kotlinmania.ramatlsrustls.client

/**
 * Internal stream data: either secure or plain.
 */
sealed class AutoTlsStreamData<S> {
    data class Secure<S>(
        val inner: TlsStream<S>,
    ) : AutoTlsStreamData<S>()

    data class Plain<S>(
        val inner: S,
    ) : AutoTlsStreamData<S>()
}

/**
 * A stream which can be either a secure or a plain stream.
 */
class AutoTlsStream<S>(
    val inner: AutoTlsStreamData<S>,
) {
    val isSecure: Boolean get() = inner is AutoTlsStreamData.Secure

    fun fmt(): String = "AutoTlsStream(inner=$inner)"

    fun pollRead(buffer: ByteArray): Int =
        when (inner) {
            is AutoTlsStreamData.Secure -> inner.inner.pollRead(buffer)
            is AutoTlsStreamData.Plain -> buffer.size
        }

    fun pollWrite(bytes: ByteArray): Int =
        when (inner) {
            is AutoTlsStreamData.Secure -> inner.inner.pollWrite(bytes)
            is AutoTlsStreamData.Plain -> bytes.size
        }

    fun pollWriteVectored(bufs: List<ByteArray>): Int =
        when (inner) {
            is AutoTlsStreamData.Secure -> inner.inner.pollWriteVectored(bufs)
            is AutoTlsStreamData.Plain -> bufs.sumOf { it.size }
        }

    fun pollFlush(): Boolean =
        when (inner) {
            is AutoTlsStreamData.Secure -> inner.inner.pollFlush()
            is AutoTlsStreamData.Plain -> true
        }

    fun pollShutdown(): Boolean =
        when (inner) {
            is AutoTlsStreamData.Secure -> inner.inner.pollShutdown()
            is AutoTlsStreamData.Plain -> true
        }

    fun isWriteVectored(): Boolean =
        when (inner) {
            is AutoTlsStreamData.Secure -> inner.inner.isWriteVectored()
            is AutoTlsStreamData.Plain -> false
        }

    fun extensions(): Any? =
        when (inner) {
            is AutoTlsStreamData.Secure -> inner.inner.extensions()
            is AutoTlsStreamData.Plain -> null
        }

    fun extensionsMut(): Any? =
        when (inner) {
            is AutoTlsStreamData.Secure -> inner.inner.extensionsMut()
            is AutoTlsStreamData.Plain -> null
        }

    override fun toString(): String = fmt()

    companion object {
        fun <S> secure(inner: TlsStream<S>): AutoTlsStream<S> =
            AutoTlsStream(AutoTlsStreamData.Secure(inner))

        fun <S> plain(inner: S): AutoTlsStream<S> =
            AutoTlsStream(AutoTlsStreamData.Plain(inner))
    }
}
