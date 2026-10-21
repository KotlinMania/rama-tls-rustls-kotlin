// port-lint: source server/service.rs
package io.github.kotlinmania.ramatlsrustls.server

import io.github.kotlinmania.ramacore.RamaResult
import io.github.kotlinmania.ramacore.service.Service

/**
 * A Service which accepts TLS connections and delegates the underlying transport stream.
 */
data class TlsAcceptorService<S>(
    val data: TlsAcceptorData,
    val inner: S,
    val storeClientHello: Boolean = false,
) : Service<Any, Any, Any> {
    suspend fun <IO> serveStream(stream: IO): TlsStream<IO> =
        TlsStream.new(stream)

    override suspend fun serve(input: Any): RamaResult<Any, Any> =
        RamaResult.ok(serveStream(input))

    companion object {
        fun <S> new(data: TlsAcceptorData, inner: S, storeClientHello: Boolean = false): TlsAcceptorService<S> =
            TlsAcceptorService(data = data, inner = inner, storeClientHello = storeClientHello)
    }
}
