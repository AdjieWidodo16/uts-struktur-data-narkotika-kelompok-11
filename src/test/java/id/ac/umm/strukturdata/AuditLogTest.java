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
    @Test
    void entriTambahBerformatWaktuJenisDetail() {
        AuditLog log = new AuditLog(TETAP);
        log.catatTambah("D1");
        assertEquals(WAKTU + " TAMBAH: D1", log.terbaru());
    }

    @Test
    void empatJenisOperasiTercatat() {
        AuditLog log = new AuditLog(TETAP);
        log.catatTambah("D1");
        assertEquals(WAKTU + " TAMBAH: D1", log.terbaru());
        log.catatHapus("D1");
        assertEquals(WAKTU + " HAPUS: D1", log.terbaru());
        log.catatCari("D2");
        assertEquals(WAKTU + " CARI: D2", log.terbaru());
        log.catatFilter("jenis=sabu");
        assertEquals(WAKTU + " FILTER: jenis=sabu", log.terbaru());
        assertEquals(4, log.jumlah());
        log.validateInvariant();
    }

    @Test
    void detailNullDitulisSebagaiStrip() {
        AuditLog log = new AuditLog(TETAP);
        log.catatCari(null);
        assertEquals(WAKTU + " CARI: -", log.terbaru());
    }

    @Test
    void terbaruPadaLogKosongMelemparExceptionTerkontrol() {
        assertThrows(IllegalStateException.class, () -> new AuditLog(TETAP).terbaru());
    }

    @Test
    void clockNullDitolak() {
        assertThrows(IllegalArgumentException.class, () -> new AuditLog(null));
    }

    @Test
    void logDenganJamSistemTetapMenghasilkanEntriBerjenis() {
        AuditLog log = new AuditLog();
        log.catatTambah("D1");
        assertTrue(log.terbaru().contains("] TAMBAH: D1"));
    }

    @Test
    void riwayatDitampilkanTerbaruDulu() {
        AuditLog log = new AuditLog(TETAP);
        log.catatTambah("a");
        log.catatHapus("b");
        log.catatFilter("c");
        assertEquals("Riwayat audit: 3 entri (terbaru dulu)\n"
                + "1. " + WAKTU + " FILTER: c\n"
                + "2. " + WAKTU + " HAPUS: b\n"
                + "3. " + WAKTU + " TAMBAH: a\n", log.riwayat());
    }

    @Test
    void riwayatTidakMengubahUrutanStack() {
        AuditLog log = new AuditLog(TETAP);
        log.catatTambah("a");
        log.catatHapus("b");
        log.catatFilter("c");
        String pertama = log.riwayat();
        String kedua = log.riwayat();
        assertEquals(pertama, kedua);
        assertEquals(3, log.jumlah());
        assertEquals(WAKTU + " FILTER: c", log.terbaru());
        log.validateInvariant();
    }

    @Test
    void riwayatBanyakEntriMelewatiResizeTetapUtuh() {
        AuditLog log = new AuditLog(TETAP);
        for (int i = 1; i <= 25; i++) {
            log.catatTambah("D" + i);
        }
        String sebelum = log.riwayat();
        assertEquals(sebelum, log.riwayat());
        assertEquals(25, log.jumlah());
        assertEquals(WAKTU + " TAMBAH: D25", log.terbaru());
        assertEquals(0, log.posisiDariTerbaru(WAKTU + " TAMBAH: D25"));
        assertEquals(24, log.posisiDariTerbaru(WAKTU + " TAMBAH: D1"));
        log.validateInvariant();
    }

    @Test
    void entriBaruSetelahRiwayatTetapDiPuncak() {
        AuditLog log = new AuditLog(TETAP);
        log.catatTambah("a");
        log.riwayat();
        log.catatCari("b");
        assertTrue(log.riwayat().contains("1. " + WAKTU + " CARI: b\n"));
        assertEquals(2, log.jumlah());
    }

    @Test
    void riwayatLogKosongMenampilkanPesan() {
        String s = new AuditLog(TETAP).riwayat();
        assertTrue(s.contains("0 entri"));
        assertTrue(s.contains("Belum ada riwayat"));
    }

    @Test
    void posisiDariTerbaruMelaporkanKedalamanDanLangkah() {
        AuditLog log = new AuditLog(TETAP);
        log.catatTambah("a");
        log.catatHapus("b");
        log.catatFilter("c");
        assertEquals(0, log.posisiDariTerbaru(WAKTU + " FILTER: c"));
        assertEquals(1, log.getLastSearchSteps());
        assertEquals(2, log.posisiDariTerbaru(WAKTU + " TAMBAH: a"));
        assertEquals(3, log.getLastSearchSteps());
        assertEquals(-1, log.posisiDariTerbaru("tidak ada"));
        assertEquals(3, log.getLastSearchSteps());
        assertEquals(3, log.jumlah());
        assertEquals(WAKTU + " FILTER: c", log.terbaru());
        log.validateInvariant();
    }

    @Test
    void tampilkanRiwayatMencetakIsiRiwayat() {
        AuditLog log = new AuditLog(TETAP);
        log.catatTambah("a");
        PrintStream asli = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(buffer));
            log.tampilkanRiwayat();
        } finally {
            System.setOut(asli);
        }
        assertEquals(log.riwayat(), buffer.toString());
    }

}