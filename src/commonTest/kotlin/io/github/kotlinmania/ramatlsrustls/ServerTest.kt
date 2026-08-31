// port-lint: tests rama-tls-rustls/src/server/acceptor_data.rs
package io.github.kotlinmania.ramatlsrustls

import io.github.kotlinmania.ramatlsrustls.server.SelfSignedData
import io.github.kotlinmania.ramatlsrustls.server.ServerConfig
import io.github.kotlinmania.ramatlsrustls.server.ServerConfigData
import io.github.kotlinmania.ramatlsrustls.server.TlsAcceptorData
import io.github.kotlinmania.ramatlsrustls.server.TlsAcceptorDataBuilder
import io.github.kotlinmania.ramatlsrustls.server.TlsAcceptorLayer
import io.github.kotlinmania.ramatlsrustls.server.TlsStream
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ServerTest {
    @Test
    fun testTlsAcceptorDataBuilder() {
        val cert = CertificateDer(byteArrayOf(1, 2, 3))
        val key = byteArrayOf(4, 5, 6)
        val builder =
            TlsAcceptorDataBuilder
                .new(listOf(cert), key)
                .withAlpnProtocolsHttpAuto()

        val data = builder.build()
        val config = data.serverConfig
        assertTrue(config is ServerConfig.Stored)
        assertEquals(1, config.config.certificates.size)
        assertEquals(2, config.config.alpnProtocols.size)
    }

    @Test
    fun testSelfSignedBuilder() {
        val selfSigned =
            TlsAcceptorDataBuilder
                .tryNewSelfSigned(SelfSignedData(listOf("localhost")))
                .withAlpnProtocols(listOf(ApplicationProtocol.HTTP_11))
                .build()

        assertTrue(selfSigned.serverConfig is ServerConfig.Stored)
    }

    @Test
    fun testTlsAcceptorLayerAndService() {
        val data = TlsAcceptorData.fromConfig(ServerConfigData())
        val layer = TlsAcceptorLayer.new(data).withStoreClientHello(true)
        assertTrue(layer.storeClientHello)

        val service = layer.layer("inner-server-service")
        assertEquals("inner-server-service", service.inner)
        assertTrue(service.storeClientHello)
    }

    @Test
    fun testServerTlsStream() {
        val stream = TlsStream("server-raw-stream")
        assertEquals("server-raw-stream", stream.getRef())
        assertEquals("server-raw-stream", stream.getMut())
    }
}
