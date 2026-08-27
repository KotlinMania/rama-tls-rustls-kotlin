// port-lint: tests verify.rs
package io.github.kotlinmania.ramatlsrustls

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

class VerifyTest {
    @Test
    fun noServerCertVerifierAcceptsServerCertificates() {
        val verifier = NoServerCertVerifier.default()

        val verified =
            verifier.verifyServerCert(
                CertificateDer(byteArrayOf(1)),
                listOf(CertificateDer(byteArrayOf(2))),
                ServerName("example.com"),
                byteArrayOf(3),
                UnixTime(42),
            )

        assertSame(ServerCertVerified.Assertion, verified)
    }

    @Test
    fun noServerCertVerifierAcceptsHandshakeSignatures() {
        val verifier = NoServerCertVerifier.new()
        val cert = CertificateDer(byteArrayOf(1))
        val dss = DigitallySignedStruct(SignatureScheme.ED25519, byteArrayOf(2))

        assertSame(
            HandshakeSignatureValid.Assertion,
            verifier.verifyTls12Signature(byteArrayOf(3), cert, dss),
        )
        assertSame(
            HandshakeSignatureValid.Assertion,
            verifier.verifyTls13Signature(byteArrayOf(4), cert, dss),
        )
    }

    @Test
    fun supportedVerifySchemesMatchRustlsVerifier() {
        assertEquals(
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
            ),
            NoServerCertVerifier.new().supportedVerifySchemes(),
        )
    }
}
