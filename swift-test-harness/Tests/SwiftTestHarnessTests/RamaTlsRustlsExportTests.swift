import Testing
import RamaTlsRustls

@Suite("RamaTlsRustls Export Tests")
struct RamaTlsRustlsExportTests {
    @Test("NoServerCertVerifier initializes successfully")
    func testNoServerCertVerifier() throws {
        let verifier = NoServerCertVerifier()
        #expect(!String(describing: verifier).isEmpty)
    }

    @Test("NoServerCertVerifier companion default and new initialize")
    func testNoServerCertVerifierCompanion() throws {
        let defaultVerifier = NoServerCertVerifier.Companion.shared.default()
        let newVerifier = NoServerCertVerifier.Companion.shared.new()
        #expect(!String(describing: defaultVerifier).isEmpty)
        #expect(!String(describing: newVerifier).isEmpty)
    }

    @Test("ServerName initializes with value")
    func testServerName() throws {
        let name = ServerName(value: "example.com")
        #expect(name.value == "example.com")
    }

    @Test("UnixTime initializes with epochSeconds")
    func testUnixTime() throws {
        let time = UnixTime(epochSeconds: 1234567890)
        #expect(time.epochSeconds == 1234567890)
    }

    @Test("RamaTlsRustlsCrateMarker initializes")
    func testRamaTlsRustlsCrateMarker() throws {
        let marker = RamaTlsRustlsCrateMarker()
        #expect(!String(describing: marker).isEmpty)
    }
}
