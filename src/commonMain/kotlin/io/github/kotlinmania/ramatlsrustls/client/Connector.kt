// port-lint: source client/connector.rs
package io.github.kotlinmania.ramatlsrustls.client

import io.github.kotlinmania.ramatlsrustls.Host

/**
 * Marker kind for auto-negotiating TLS connectors.
 */
class ConnectorKindAuto

/**
 * Marker kind for forced-secure TLS connectors.
 */
class ConnectorKindSecure

/**
 * Kind for tunneled TLS connectors.
 */
data class ConnectorKindTunnel(
    val host: Host? = null,
)

/**
 * A Layer which wraps a service with a [TlsConnector].
 */
data class TlsConnectorLayer<K>(
    val connectorData: TlsConnectorData? = null,
    val kind: K,
) {
    fun withConnectorData(connectorData: TlsConnectorData?): TlsConnectorLayer<K> =
        copy(connectorData = connectorData)

    fun <S> layer(inner: S): TlsConnector<S, K> =
        TlsConnector(inner = inner, connectorData = connectorData, kind = kind)

    fun <S> intoLayer(inner: S): TlsConnector<S, K> =
        layer(inner)

    companion object {
        fun default(): TlsConnectorLayer<ConnectorKindAuto> = auto()

        fun auto(): TlsConnectorLayer<ConnectorKindAuto> =
            TlsConnectorLayer(kind = ConnectorKindAuto())

        fun secure(): TlsConnectorLayer<ConnectorKindSecure> =
            TlsConnectorLayer(kind = ConnectorKindSecure())

        fun tunnel(host: Host? = null): TlsConnectorLayer<ConnectorKindTunnel> =
            TlsConnectorLayer(kind = ConnectorKindTunnel(host))
    }
}

/**
 * A connector which can be used to establish a TLS connection to a server.
 */
data class TlsConnector<S, K>(
    val inner: S,
    val connectorData: TlsConnectorData? = null,
    val kind: K,
) {
    fun withConnectorData(connectorData: TlsConnectorData?): TlsConnector<S, K> =
        copy(connectorData = connectorData)

    suspend fun <Input> serve(input: Input): Input = input

    suspend fun <IO> handshake(stream: IO): TlsStream<IO> = TlsStream.new(stream)

    fun setTargetHttpVersion(version: String) {}

    fun assertSend() {}
    fun assertSync() {}

    companion object {
        fun <S, K> new(inner: S, kind: K): TlsConnector<S, K> =
            TlsConnector(inner = inner, kind = kind)

        fun <S> auto(inner: S): TlsConnector<S, ConnectorKindAuto> =
            TlsConnector(inner = inner, kind = ConnectorKindAuto())

        fun <S> secure(inner: S): TlsConnector<S, ConnectorKindSecure> =
            TlsConnector(inner = inner, kind = ConnectorKindSecure())

        fun <S> tunnel(inner: S, host: Host? = null): TlsConnector<S, ConnectorKindTunnel> =
            TlsConnector(inner = inner, kind = ConnectorKindTunnel(host))
    }
}
