package id.ac.umm.strukturdata;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Test;

class AuditLogTest {

    // Jam tetap supaya waktu pada entri bisa diprediksi di test.
    private static final Clock TETAP =
            Clock.fixed(Instant.parse("2024-05-20T03:04:05Z"), ZoneOffset.UTC);
    private static final String WAKTU = "[2024-05-20 03:04:05]";

    @Test
    void logBaruKosong() {
        AuditLog log = new AuditLog();
        assertEquals(0, log.jumlah());
        assertTrue(log.isEmpty());
        log.validateInvariant();
    }

    @Test
    void catatMenambahJumlahEntri() {
        AuditLog log = new AuditLog();
        log.catat("a");
        log.catat("b");
        assertEquals(2, log.jumlah());
        assertFalse(log.isEmpty());
        log.validateInvariant();
    }

    @Test
    void entriKosongAtauNullDitolakDanJumlahTidakBerubah() {
        AuditLog log = new AuditLog();
        log.catat("a");
        assertThrows(IllegalArgumentException.class, () -> log.catat(null));
        assertThrows(IllegalArgumentException.class, () -> log.catat("   "));
        assertEquals(1, log.jumlah());
        log.validateInvariant();
    }
}