// port-lint: source type_conversion.rs
package io.github.kotlinmania.ramatlsrustls

/**
 * Protocol version for TLS connections.
 */
sealed class ProtocolVersion(
    val code: UShort,
) {
    data object TLS12 : ProtocolVersion(0x0303u)

    data object TLS13 : ProtocolVersion(0x0304u)

    class Unknown(
        code: UShort,
    ) : ProtocolVersion(code)

    companion object {
        fun fromCode(code: UShort): ProtocolVersion =
            when (code) {
                0x0303u.toUShort() -> TLS12
                0x0304u.toUShort() -> TLS13
                else -> Unknown(code)
            }

        fun ramaFrom(code: UShort): ProtocolVersion = fromCode(code)
    }

    fun ramaInto(): UShort = code

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ProtocolVersion) return false
        return code == other.code
    }

    override fun hashCode(): Int = code.hashCode()

    override fun toString(): String =
        when (this) {
            TLS12 -> "TLS12"
            TLS13 -> "TLS13"
            is Unknown -> "Unknown($code)"
        }
}

/**
 * Cipher suite identifier.
 */
data class CipherSuite(
    val code: UShort,
) {
    companion object {
        val TLS_AES_128_GCM_SHA256 = CipherSuite(0x1301u)
        val TLS_AES_256_GCM_SHA384 = CipherSuite(0x1302u)
        val TLS_CHACHA20_POLY1305_SHA256 = CipherSuite(0x1303u)
        val TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256 = CipherSuite(0xC02Bu)
        val TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256 = CipherSuite(0xC02Fu)
        val TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384 = CipherSuite(0xC02Cu)
        val TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384 = CipherSuite(0xC030u)

        fun ramaFrom(code: UShort): CipherSuite = CipherSuite(code)
    }

    fun ramaInto(): UShort = code
}

/**
 * Application protocol negotiated via ALPN.
 */
data class ApplicationProtocol(
    val bytes: ByteArray,
) {
    val name: String get() = bytes.decodeToString()

    fun isSecure(): Boolean = true

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ApplicationProtocol) return false
        return bytes.contentEquals(other.bytes)
    }

    override fun hashCode(): Int = bytes.contentHashCode()

    override fun toString(): String = name

    companion object {
        val HTTP_11 = ApplicationProtocol("http/1.1".encodeToByteArray())
        val HTTP_2 = ApplicationProtocol("h2".encodeToByteArray())
        val HTTP_3 = ApplicationProtocol("h3".encodeToByteArray())

        fun fromString(protocol: String): ApplicationProtocol =
            ApplicationProtocol(protocol.encodeToByteArray())

        fun ramaFrom(protocol: String): ApplicationProtocol = fromString(protocol)
    }
}

/**
 * Host name or IP address.
 */
sealed class Host {
    data class Name(
        val domain: String,
    ) : Host()

    data class Address(
        val ip: String,
    ) : Host()

    companion object {
        fun fromDomain(domain: String): Host = Name(domain)

        fun fromIp(ip: String): Host = Address(ip)
    }
}

/**
 * Data encoding for certificates and DER payloads.
 */
sealed class DataEncoding {
    data class Der(
        val bytes: ByteArray,
    ) : DataEncoding() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Der) return false
            return bytes.contentEquals(other.bytes)
        }

        override fun hashCode(): Int = bytes.contentHashCode()
    }

    data class DerStack(
        val items: List<ByteArray>,
    ) : DataEncoding()

    companion object {
        fun fromCertificate(cert: CertificateDer): DataEncoding = Der(cert.bytes)

        fun fromCertificates(certs: List<CertificateDer>): DataEncoding =
            DerStack(certs.map { it.bytes })
    }
}

/**
 * TLS Client Hello extensions.
 */
sealed class ClientHelloExtension {
    data class SignatureAlgorithms(
        val schemes: List<SignatureScheme>,
    ) : ClientHelloExtension()

    data class ServerName(
        val name: String?,
    ) : ClientHelloExtension()

    data class ApplicationLayerProtocolNegotiation(
        val protocols: List<ApplicationProtocol>,
    ) : ClientHelloExtension()
}

/**
 * TLS Client Hello representation.
 */
data class ClientHello(
    val protocolVersion: ProtocolVersion = ProtocolVersion.Unknown(0u),
    val cipherSuites: List<CipherSuite> = emptyList(),
    val supportedGroups: List<UShort> = emptyList(),
    val extensions: List<ClientHelloExtension> = emptyList(),
) {
    fun signatureSchemes(): List<SignatureScheme> =
        extensions
            .filterIsInstance<ClientHelloExtension.SignatureAlgorithms>()
            .flatMap { it.schemes }

    fun serverName(): String? =
        extensions
            .filterIsInstance<ClientHelloExtension.ServerName>()
            .firstOrNull()
            ?.name

    fun alpn(): List<ApplicationProtocol>? =
        extensions
            .filterIsInstance<ClientHelloExtension.ApplicationLayerProtocolNegotiation>()
            .firstOrNull()
            ?.protocols
}

/**
 * Converts a [ServerName] into a [Host].
 */
fun ServerName.toHost(): Host = Host.Name(value)

/**
 * Converts a [Host] into a [ServerName].
 */
fun Host.toServerName(): ServerName =
    when (this) {
        is Host.Name -> ServerName(domain)
        is Host.Address -> ServerName(ip)
    }

/**
 * Converts a [CertificateDer] to [DataEncoding].
 */
fun CertificateDer.toDataEncoding(): DataEncoding = DataEncoding.Der(bytes)

/**
 * Converts a list of [CertificateDer] to [DataEncoding].
 */
fun List<CertificateDer>.toDataEncoding(): DataEncoding =
    DataEncoding.DerStack(map { it.bytes })

/**
 * Rama TryFrom conversion helper for [ProtocolVersion].
 */
fun ProtocolVersion.ramaTryFrom(): ProtocolVersion? =
    when (this) {
        ProtocolVersion.TLS12, ProtocolVersion.TLS13 -> this
        is ProtocolVersion.Unknown -> null
    }

/**
 * Rama TryFrom conversion helper from [Host] to [ServerName].
 */
fun Host.ramaTryFrom(): ServerName = toServerName()

/**
 * Rama TryFrom conversion helper from [ServerName] to [Host].
 */
fun ServerName.ramaTryFrom(): Host = toHost()
