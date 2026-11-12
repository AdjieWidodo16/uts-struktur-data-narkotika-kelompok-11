package id.ac.umm.strukturdata;

/**
 * Filter putusan. Kriteria yang tidak diisi dianggap tidak aktif.
 * Jika beberapa kriteria aktif, putusan harus lolos SEMUANYA (logika AND).
 * Hasil filter adalah SinglyLinkedList baru; list asli tidak diubah.
 */
public class PutusanFilter {

    private String jenisNarkotika;   // null = kriteria tidak aktif

    /** Jenis narkotika mengandung teks ini (tanpa membedakan huruf besar/kecil). */
    public PutusanFilter jenisNarkotika(String teks) {
        if (teks == null || teks.trim().isEmpty()) {
            throw new IllegalArgumentException("Kriteria jenis narkotika tidak boleh kosong");
        }
        this.jenisNarkotika = teks.trim();
        return this;
    }

    public SinglyLinkedList<Putusan> terapkan(SinglyLinkedList<Putusan> list) {
        if (list == null) {
            throw new IllegalArgumentException("List putusan tidak boleh null");
        }
        SinglyLinkedList<Putusan> hasil = new SinglyLinkedList<>();
        Node<Putusan> cur = list.getHead();
        while (cur != null) {
            if (cocok(cur.data)) {
                hasil.addLast(cur.data);
            }
            cur = cur.next;
        }
        return hasil;
    }

    private boolean cocok(Putusan p) {
        if (p == null) {
            return false;
        }
        if (jenisNarkotika != null && !mengandung(p.getJenisNarkotika(), jenisNarkotika)) {
            return false;
        }
        return true;
    }

    /** Pencarian substring tanpa membedakan huruf besar/kecil; teks null tidak pernah cocok. */
    private static boolean mengandung(String teks, String kata) {
        if (teks == null) {
            return false;
        }
        int batas = teks.length() - kata.length();
        for (int i = 0; i <= batas; i++) {
            if (teks.regionMatches(true, i, kata, 0, kata.length())) {
                return true;
            }
        }
        return false;
    }
}