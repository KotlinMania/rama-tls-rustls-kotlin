// port-lint: tests rama-tls-rustls/src/key_log.rs
package io.github.kotlinmania.ramatlsrustls

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class KeyLogTest {

    @Test
    fun testKeyLogFile() {
        val handle = KeyLogFileHandle("/tmp/ssl_keys.log")
        val keyLog = KeyLogFile(handle)

        val clientRandom = byteArrayOf(0x01, 0x02, 0x03, 0x0a, 0x0f)
        val secret = byteArrayOf(0xde.toByte(), 0xad.toByte(), 0xbe.toByte(), 0xef.toByte())

        keyLog.log("CLIENT_RANDOM", clientRandom, secret)

        val expected = "CLIENT_RANDOM 0102030a0f deadbeef\n"
        assertEquals(expected, handle.content())
    }

    @Test
    fun testKeyLogFileTryNew() {
        val keyLog = KeyLogFile.tryNew("/tmp/keys.log")
        assertEquals("/tmp/keys.log", keyLog.handle.path)
    }
}
