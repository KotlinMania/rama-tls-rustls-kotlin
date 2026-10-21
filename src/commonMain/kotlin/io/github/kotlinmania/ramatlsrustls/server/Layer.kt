// port-lint: source server/layer.rs
package io.github.kotlinmania.ramatlsrustls.server

import io.github.kotlinmania.ramacore.layer.Layer

/**
 * A Layer which wraps the given service with a [TlsAcceptorService].
 */
data class TlsAcceptorLayer(
    val data: TlsAcceptorData,
    val storeClientHello: Boolean = false,
) : Layer<Any?, TlsAcceptorService<Any?>> {
    fun withStoreClientHello(store: Boolean): TlsAcceptorLayer =
        copy(storeClientHello = store)

    fun setStoreClientHello(store: Boolean): TlsAcceptorLayer =
        copy(storeClientHello = store)

    override fun layer(inner: Any?): TlsAcceptorService<Any?> =
        TlsAcceptorService(data = data, inner = inner, storeClientHello = storeClientHello)

    override fun intoLayer(inner: Any?): TlsAcceptorService<Any?> =
        layer(inner)

    fun <S> layerTyped(inner: S): TlsAcceptorService<S> =
        TlsAcceptorService(data = data, inner = inner, storeClientHello = storeClientHello)

    fun <S> intoLayerTyped(inner: S): TlsAcceptorService<S> =
        layerTyped(inner)

    companion object {
        fun new(data: TlsAcceptorData): TlsAcceptorLayer =
            TlsAcceptorLayer(data = data)
    }
}
