// port-lint: source server/service.rs
package io.github.kotlinmania.ramatlsrustls.server

/**
 * A Service which accepts TLS connections and delegates the underlying transport stream.
 */
data class TlsAcceptorService<S>(
    val data: TlsAcceptorData,
    val inner: S,
    val storeClientHello: Boolean = false,
) {
    suspend fun <IO> serve(stream: IO): TlsStream<IO> =
        TlsStream.new(stream)

    companion object {
        fun <S> new(data: TlsAcceptorData, inner: S, storeClientHello: Boolean = false): TlsAcceptorService<S> =
            TlsAcceptorService(data = data, inner = inner, storeClientHello = storeClientHello)
    }
}
