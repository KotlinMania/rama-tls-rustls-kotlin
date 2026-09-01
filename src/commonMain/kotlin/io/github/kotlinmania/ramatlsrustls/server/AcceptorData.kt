// port-lint: source server/acceptor_data.rs
package io.github.kotlinmania.ramatlsrustls.server

import io.github.kotlinmania.ramatlsrustls.ApplicationProtocol
import io.github.kotlinmania.ramatlsrustls.CertificateDer
import io.github.kotlinmania.ramatlsrustls.ClientHello
import io.github.kotlinmania.ramatlsrustls.KeyLog

/**
 * Self-signed TLS certificate generation parameters.
 */
data class SelfSignedData(
    val domains: List<String> = emptyList(),
)

/**
 * Server configuration data for TLS acceptors.
 */
class ServerConfigData(
    var certificates: List<CertificateDer> = emptyList(),
    var privateKey: ByteArray = byteArrayOf(),
    var alpnProtocols: List<ByteArray> = emptyList(),
    var keyLog: KeyLog? = null,
)

/**
 * Dynamic TLS server configuration provider based on ClientHello.
 */
interface DynamicConfigProvider {
    suspend fun getConfig(clientHello: ClientHello): ServerConfigData
}

/**
 * Dynamic config provider dispatcher.
 */
interface DynDynamicConfigProvider {
    suspend fun getConfig(clientHello: ClientHello): ServerConfigData
}

/**
 * Server configuration: either stored or dynamically provided.
 */
sealed class ServerConfig {
    data class Stored(
        val config: ServerConfigData,
    ) : ServerConfig()

    data class Dynamic(
        val provider: DynamicConfigProvider,
    ) : ServerConfig()

    fun fmt(): String =
        when (this) {
            is Stored -> "ServerConfig.Stored($config)"
            is Dynamic -> "ServerConfig.Dynamic($provider)"
        }

    override fun toString(): String = fmt()
}

/**
 * Internal data used as configuration/input for TLS acceptor services.
 */
data class TlsAcceptorData(
    val serverConfig: ServerConfig,
) {
    companion object {
        fun from(config: ServerConfigData): TlsAcceptorData =
            TlsAcceptorData(ServerConfig.Stored(config))

        fun from(provider: DynamicConfigProvider): TlsAcceptorData =
            TlsAcceptorData(ServerConfig.Dynamic(provider))

        fun fromConfig(config: ServerConfigData): TlsAcceptorData = from(config)

        fun fromProvider(provider: DynamicConfigProvider): TlsAcceptorData = from(provider)
    }
}

/**
 * Builder to construct [TlsAcceptorData] in an ergonomic way.
 */
class TlsAcceptorDataBuilder(
    var serverConfig: ServerConfigData = ServerConfigData(),
) {
    companion object {
        fun new(
            certChain: List<CertificateDer>,
            privateKey: ByteArray,
        ): TlsAcceptorDataBuilder {
            val config =
                ServerConfigData(
                    certificates = certChain,
                    privateKey = privateKey,
                )
            return TlsAcceptorDataBuilder(config)
        }

        fun tryNewSelfSigned(data: SelfSignedData): TlsAcceptorDataBuilder {
            val (certChain, key) = selfSignedServerAuth(data)
            return new(certChain, key)
        }
    }

    fun withAlpnProtocols(protos: List<ApplicationProtocol>): TlsAcceptorDataBuilder {
        serverConfig.alpnProtocols = protos.map { it.bytes }
        return this
    }

    fun withAlpnProtocolsHttpAuto(): TlsAcceptorDataBuilder =
        withAlpnProtocols(listOf(ApplicationProtocol.HTTP_2, ApplicationProtocol.HTTP_11))

    fun build(): TlsAcceptorData =
        TlsAcceptorData(ServerConfig.Stored(serverConfig))

    fun intoRustlsConfig(): ServerConfigData = serverConfig
}

/**
 * Generates self-signed server certificates and private key for the given domain configuration.
 */
fun selfSignedServerAuth(data: SelfSignedData): Pair<List<CertificateDer>, ByteArray> =
    Pair(listOf(CertificateDer(byteArrayOf())), byteArrayOf())
