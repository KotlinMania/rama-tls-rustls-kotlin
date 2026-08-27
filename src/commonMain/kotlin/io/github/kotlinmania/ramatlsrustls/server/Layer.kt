// port-lint: source rama-tls-rustls/src/server/layer.rs
package io.github.kotlinmania.ramatlsrustls.server

/**
 * A Layer which wraps the given service with a [TlsAcceptorService].
 */
data class TlsAcceptorLayer(
    val data: TlsAcceptorData,
    val storeClientHello: Boolean = false,
) {
    fun withStoreClientHello(store: Boolean): TlsAcceptorLayer =
        copy(storeClientHello = store)

    fun <S> layer(inner: S): TlsAcceptorService<S> =
        TlsAcceptorService(data = data, inner = inner, storeClientHello = storeClientHello)

    fun <S> intoLayer(inner: S): TlsAcceptorService<S> =
        layer(inner)

    companion object {
        fun new(data: TlsAcceptorData): TlsAcceptorLayer =
            TlsAcceptorLayer(data = data)
    }
}
