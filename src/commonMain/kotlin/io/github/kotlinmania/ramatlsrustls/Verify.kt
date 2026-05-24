// port-lint: source verify.rs
package io.github.kotlinmania.ramatlsrustls

/**
 * TLS Verify support for Rustls usage in Rama.
 *
 * ... or rather the lack of verification where it is not needed.
 */

/** Certificate verifier that does not verify the server certificate. */
class NoServerCertVerifier {
    companion object {
        /** Create a new instance of [NoServerCertVerifier]. */
        fun new(): NoServerCertVerifier = NoServerCertVerifier()

        fun default(): NoServerCertVerifier = new()
    }

    fun verifyServerCert(
        _endEntity: CertificateDer,
        _intermediates: List<CertificateDer>,
        _serverName: ServerName,
        _ocspResponse: ByteArray,
        _now: UnixTime,
    ): ServerCertVerified {
        return ServerCertVerified.Assertion
    }

    fun verifyTls12Signature(
        _message: ByteArray,
        _cert: CertificateDer,
        _dss: DigitallySignedStruct,
    ): HandshakeSignatureValid {
        return HandshakeSignatureValid.Assertion
    }

    fun verifyTls13Signature(
        _message: ByteArray,
        _cert: CertificateDer,
        _dss: DigitallySignedStruct,
    ): HandshakeSignatureValid {
        return HandshakeSignatureValid.Assertion
    }

    /** Returns every signature scheme accepted by this verifier. */
    fun supportedVerifySchemes(): List<SignatureScheme> =
        listOf(
            SignatureScheme.RSA_PKCS1_SHA1,
            SignatureScheme.ECDSA_SHA1_LEGACY,
            SignatureScheme.RSA_PKCS1_SHA256,
            SignatureScheme.ECDSA_NISTP256_SHA256,
            SignatureScheme.RSA_PKCS1_SHA384,
            SignatureScheme.ECDSA_NISTP384_SHA384,
            SignatureScheme.RSA_PKCS1_SHA512,
            SignatureScheme.ECDSA_NISTP521_SHA512,
            SignatureScheme.RSA_PSS_SHA256,
            SignatureScheme.RSA_PSS_SHA384,
            SignatureScheme.RSA_PSS_SHA512,
            SignatureScheme.ED25519,
            SignatureScheme.ED448,
        )
}

class CertificateDer(val bytes: ByteArray)

class ServerName(val value: String)

class UnixTime(val epochSeconds: Long)

class DigitallySignedStruct(
    val scheme: SignatureScheme,
    val bytes: ByteArray,
)

enum class SignatureScheme {
    RSA_PKCS1_SHA1,
    ECDSA_SHA1_LEGACY,
    RSA_PKCS1_SHA256,
    ECDSA_NISTP256_SHA256,
    RSA_PKCS1_SHA384,
    ECDSA_NISTP384_SHA384,
    RSA_PKCS1_SHA512,
    ECDSA_NISTP521_SHA512,
    RSA_PSS_SHA256,
    RSA_PSS_SHA384,
    RSA_PSS_SHA512,
    ED25519,
    ED448,
}

sealed interface ServerCertVerified {
    data object Assertion : ServerCertVerified
}

sealed interface HandshakeSignatureValid {
    data object Assertion : HandshakeSignatureValid
}
