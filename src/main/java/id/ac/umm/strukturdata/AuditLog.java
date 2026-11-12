package id.ac.umm.strukturdata;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Riwayat operasi pengguna (fitur Audit), disimpan di Stack buatan sendiri.
 * Entri terbaru berada di puncak stack.
 */
public class AuditLog {

    public static final String TAMBAH = "TAMBAH";
    public static final String HAPUS = "HAPUS";
    public static final String CARI = "CARI";
    public static final String FILTER = "FILTER";

    private static final DateTimeFormatter FORMAT_WAKTU =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final Clock clock;
    private final Stack<String> riwayat = new Stack<>();

    public AuditLog() {
        this(Clock.systemDefaultZone());
    }

    /** Clock disuntik agar waktu entri bisa dites secara deterministik. */
    public AuditLog(Clock clock) {
        if (clock == null) {
            throw new IllegalArgumentException("Clock tidak boleh null");
        }
        this.clock = clock;
    }

    /** Mencatat satu entri mentah ke puncak stack. */
    public void catat(String entri) {
        if (entri == null || entri.trim().isEmpty()) {
            throw new IllegalArgumentException("Entri audit tidak boleh kosong");
        }
        riwayat.push(entri);
    }

    public void catatTambah(String detail) {
        catatOperasi(TAMBAH, detail);
    }

    public void catatHapus(String detail) {
        catatOperasi(HAPUS, detail);
    }

    public void catatCari(String detail) {
        catatOperasi(CARI, detail);
    }

    public void catatFilter(String detail) {
        catatOperasi(FILTER, detail);
    }

    /** Entri terbaru (puncak stack), tanpa menghapusnya. */
    public String terbaru() {
        return riwayat.peek();
    }

    // Format entri: [yyyy-MM-dd HH:mm:ss] JENIS: detail
    private void catatOperasi(String jenis, String detail) {
        String waktu = LocalDateTime.now(clock).format(FORMAT_WAKTU);
        catat("[" + waktu + "] " + jenis + ": " + (detail == null ? "-" : detail));
    }

    public int jumlah() {
        return riwayat.size();
    }

    public boolean isEmpty() {
        return riwayat.isEmpty();
    }

    public void validateInvariant() {
        riwayat.validateInvariant();
    }
}