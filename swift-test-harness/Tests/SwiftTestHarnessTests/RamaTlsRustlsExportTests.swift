import Testing
import RamaTlsRustls

@Suite("RamaTlsRustls Export Smoke Tests")
struct RamaTlsRustlsExportTests {
    @Test("Swift module loads cleanly")
    func testSwiftModuleLoads() throws {
        #expect(true)
    }
}
