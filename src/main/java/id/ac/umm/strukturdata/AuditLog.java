package id.ac.umm.strukturdata;

/**
 * Riwayat operasi pengguna (fitur Audit), disimpan di Stack buatan sendiri.
 * Entri terbaru berada di puncak stack.
 */
public class AuditLog {

    private final Stack<String> riwayat = new Stack<>();

    /** Mencatat satu entri mentah ke puncak stack. */
    public void catat(String entri) {
        if (entri == null || entri.trim().isEmpty()) {
            throw new IllegalArgumentException("Entri audit tidak boleh kosong");
        }
        riwayat.push(entri);
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