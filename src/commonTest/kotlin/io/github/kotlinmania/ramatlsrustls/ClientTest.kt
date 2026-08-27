// port-lint: tests client/mod.rs
package io.github.kotlinmania.ramatlsrustls

import io.github.kotlinmania.ramatlsrustls.client.AutoTlsStream
import io.github.kotlinmania.ramatlsrustls.client.ConnectorKindAuto
import io.github.kotlinmania.ramatlsrustls.client.ConnectorKindSecure
import io.github.kotlinmania.ramatlsrustls.client.ConnectorKindTunnel
import io.github.kotlinmania.ramatlsrustls.client.TlsConnector
import io.github.kotlinmania.ramatlsrustls.client.TlsConnectorData
import io.github.kotlinmania.ramatlsrustls.client.TlsConnectorDataBuilder
import io.github.kotlinmania.ramatlsrustls.client.TlsConnectorLayer
import io.github.kotlinmania.ramatlsrustls.client.TlsStream
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class ClientTest {

    @Test
    fun testTlsConnectorDataBuilders() {
        val httpAuto = TlsConnectorData.tryNewHttpAuto()
        assertNotNull(httpAuto.clientConfig)
        assertEquals(2, httpAuto.clientConfig.alpnProtocols.size)

        val http1 = TlsConnectorData.tryNewHttp1()
        assertEquals(1, http1.clientConfig.alpnProtocols.size)

        val http2 = TlsConnectorData.tryNewHttp2()
        assertEquals(1, http2.clientConfig.alpnProtocols.size)

        val custom = TlsConnectorDataBuilder.new()
            .withServerName(Host.fromDomain("example.org"))
            .withStoreServerCertificateChain(true)
            .withNoCertVerifier()
            .build()

        assertEquals(Host.Name("example.org"), custom.serverName)
        assertTrue(custom.storeServerCertificateChain)
        assertNotNull(custom.clientConfig.serverCertVerifier)
    }

    @Test
    fun testAutoTlsStream() {
        val plainStream = AutoTlsStream.plain("plain-socket")
        assertFalse(plainStream.isSecure)

        val secureStream = AutoTlsStream.secure(TlsStream("secure-socket"))
        assertTrue(secureStream.isSecure)
    }

    @Test
    fun testTlsConnectorAndLayer() {
        val autoLayer = TlsConnectorLayer.auto()
        assertNotNull(autoLayer.kind)

        val secureLayer = TlsConnectorLayer.secure()
        assertNotNull(secureLayer.kind)

        val tunnelLayer = TlsConnectorLayer.tunnel(Host.fromDomain("proxy.net"))
        assertEquals(Host.Name("proxy.net"), tunnelLayer.kind.host)

        val connector = autoLayer.layer("inner-service")
        assertEquals("inner-service", connector.inner)
    }
}
