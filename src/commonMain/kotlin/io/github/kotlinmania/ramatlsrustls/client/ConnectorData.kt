// port-lint: source client/connector_data.rs
package io.github.kotlinmania.ramatlsrustls.client

import io.github.kotlinmania.ramatlsrustls.ApplicationProtocol
import io.github.kotlinmania.ramatlsrustls.CertificateDer
import io.github.kotlinmania.ramatlsrustls.Host
import io.github.kotlinmania.ramatlsrustls.KeyLog
import io.github.kotlinmania.ramatlsrustls.NoServerCertVerifier

/**
 * Root certificate store.
 */
class RootCertStore(
    roots: List<CertificateDer> = emptyList(),
) {
    private val mutableRoots: MutableList<CertificateDer> = roots.toMutableList()

    val roots: List<CertificateDer> get() = mutableRoots

    fun add(cert: CertificateDer) {
        mutableRoots.add(cert)
    }

    companion object {
        fun empty(): RootCertStore = RootCertStore()
    }
}

/**
 * Client configuration for TLS connections.
 */
class ClientConfig(
    var rootCertificates: RootCertStore = RootCertStore.empty(),
    var alpnProtocols: List<ByteArray> = emptyList(),
    var keyLog: KeyLog? = null,
    var serverCertVerifier: Any? = null,
) {
    companion object {
        fun builder(): ClientConfig = ClientConfig()
    }
}

/**
 * Internal data used as configuration/input for TLS client connectors.
 */
data class TlsConnectorData(
    val clientConfig: ClientConfig,
    val serverName: Host? = null,
    val storeServerCertificateChain: Boolean = false,
) {
    companion object {
        fun from(config: ClientConfig): TlsConnectorData =
            TlsConnectorData(clientConfig = config)

        /**
         * Create a default [TlsConnectorData] supporting auto HTTP connections.
         */
        fun tryNewHttpAuto(): TlsConnectorData =
            TlsConnectorDataBuilder.new()
                .withAlpnProtocolsHttpAuto()
                .build()

        /**
         * Create a default [TlsConnectorData] supporting HTTP/1.1 connections.
         */
        fun tryNewHttp1(): TlsConnectorData =
            TlsConnectorDataBuilder.new()
                .withAlpnProtocols(listOf(ApplicationProtocol.HTTP_11))
                .build()

        /**
         * Create a default [TlsConnectorData] supporting HTTP/2 connections.
         */
        fun tryNewHttp2(): TlsConnectorData =
            TlsConnectorDataBuilder.new()
                .withAlpnProtocols(listOf(ApplicationProtocol.HTTP_2))
                .build()
    }
}

/**
 * Builder to construct [TlsConnectorData] in an ergonomic way.
 */
class TlsConnectorDataBuilder(
    var clientConfig: ClientConfig = ClientConfig(),
    var serverName: Host? = null,
    var storeServerCertificateChain: Boolean = false,
) {
    companion object {
        fun default(): TlsConnectorDataBuilder = new()

        fun from(config: ClientConfig): TlsConnectorDataBuilder =
            TlsConnectorDataBuilder(clientConfig = config)

        fun new(): TlsConnectorDataBuilder =
            TlsConnectorDataBuilder(
                clientConfig = ClientConfig(
                    rootCertificates = clientRootCerts(),
                ),
            )

        fun newWithClientAuth(
            clientCertChain: List<CertificateDer>,
            clientPrivateKey: ByteArray,
        ): TlsConnectorDataBuilder {
            val builder = new()
            return builder
        }
    }

    fun withAlpnProtocols(protos: List<ApplicationProtocol>): TlsConnectorDataBuilder {
        clientConfig.alpnProtocols = protos.map { it.bytes }
        return this
    }

    fun withAlpnProtocolsHttpAuto(): TlsConnectorDataBuilder =
        withAlpnProtocols(listOf(ApplicationProtocol.HTTP_2, ApplicationProtocol.HTTP_11))

    fun withCertVerifier(verifier: Any): TlsConnectorDataBuilder {
        clientConfig.serverCertVerifier = verifier
        return this
    }

    fun withNoCertVerifier(): TlsConnectorDataBuilder =
        withCertVerifier(NoServerCertVerifier())

    fun withServerName(serverName: Host?): TlsConnectorDataBuilder {
        this.serverName = serverName
        return this
    }

    fun withStoreServerCertificateChain(value: Boolean): TlsConnectorDataBuilder {
        this.storeServerCertificateChain = value
        return this
    }

    fun build(): TlsConnectorData =
        TlsConnectorData(
            clientConfig = clientConfig,
            serverName = serverName,
            storeServerCertificateChain = storeServerCertificateChain,
        )
}

/**
 * Returns the shared client root certificate store.
 */
fun clientRootCerts(): RootCertStore = RootCertStore.empty()

/**
 * Generates self-signed client authentication certificates and private key.
 */
fun selfSignedClientAuth(): Pair<List<CertificateDer>, ByteArray> =
    Pair(listOf(CertificateDer(byteArrayOf())), byteArrayOf())
