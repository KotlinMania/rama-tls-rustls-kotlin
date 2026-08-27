// port-lint: tests rama-tls-rustls/src/type_conversion.rs
package io.github.kotlinmania.ramatlsrustls

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TypeConversionTest {

    @Test
    fun testRustlsToCommonToRustls() {
        val p = ProtocolVersion.TLS13
        val converted = ProtocolVersion.ramaFrom(p.ramaInto())
        assertEquals(p, converted)
    }

    @Test
    fun testProtocolVersionConversion() {
        val p = ProtocolVersion.TLS13
        assertEquals(0x0304u.toUShort(), p.code)
        assertEquals(ProtocolVersion.TLS13, ProtocolVersion.fromCode(0x0304u))
        assertEquals(ProtocolVersion.TLS12, ProtocolVersion.fromCode(0x0303u))

        val unknown = ProtocolVersion.fromCode(0x0301u)
        assertTrue(unknown is ProtocolVersion.Unknown)
        assertEquals(0x0301u.toUShort(), unknown.code)
    }

    @Test
    fun testHostAndServerNameConversion() {
        val domainHost = Host.fromDomain("example.com")
        val serverName = domainHost.toServerName()
        assertEquals("example.com", serverName.value)
        assertEquals(Host.Name("example.com"), serverName.toHost())

        val ipHost = Host.fromIp("127.0.0.1")
        val ipServerName = ipHost.toServerName()
        assertEquals("127.0.0.1", ipServerName.value)
    }

    @Test
    fun testDataEncodingConversion() {
        val cert = CertificateDer(byteArrayOf(1, 2, 3, 4))
        val encoding = cert.toDataEncoding()
        assertTrue(encoding is DataEncoding.Der)
        assertTrue(byteArrayOf(1, 2, 3, 4).contentEquals(encoding.bytes))

        val certList = listOf(cert, CertificateDer(byteArrayOf(5, 6)))
        val stackEncoding = certList.toDataEncoding()
        assertTrue(stackEncoding is DataEncoding.DerStack)
        assertEquals(2, stackEncoding.items.size)
    }

    @Test
    fun testClientHelloExtensions() {
        val schemes = listOf(SignatureScheme.ED25519, SignatureScheme.RSA_PSS_SHA256)
        val protos = listOf(ApplicationProtocol.HTTP_2, ApplicationProtocol.HTTP_11)
        val hello = ClientHello(
            protocolVersion = ProtocolVersion.TLS13,
            cipherSuites = listOf(CipherSuite.TLS_AES_128_GCM_SHA256),
            extensions = listOf(
                ClientHelloExtension.SignatureAlgorithms(schemes),
                ClientHelloExtension.ServerName("api.example.com"),
                ClientHelloExtension.ApplicationLayerProtocolNegotiation(protos),
            ),
        )

        assertEquals(schemes, hello.signatureSchemes())
        assertEquals("api.example.com", hello.serverName())
        assertEquals(protos, hello.alpn())
    }
}
