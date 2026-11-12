package id.ac.umm.strukturdata;

/**
 * Filter putusan. Kriteria yang tidak diisi dianggap tidak aktif.
 * Jika beberapa kriteria aktif, putusan harus lolos SEMUANYA (logika AND).
 * Hasil filter adalah SinglyLinkedList baru; list asli tidak diubah.
 */
public class PutusanFilter {

    private String jenisNarkotika;   // null = kriteria tidak aktif
    private boolean beratAktif;
    private double beratMin;
    private double beratMax;
    private String pasalTerbukti;   // null = kriteria tidak aktif
    private boolean vonisAktif;
    private int vonisMin;
    private int vonisMax;

    /** Jenis narkotika mengandung teks ini (tanpa membedakan huruf besar/kecil). */
    public PutusanFilter jenisNarkotika(String teks) {
        if (teks == null || teks.trim().isEmpty()) {
            throw new IllegalArgumentException("Kriteria jenis narkotika tidak boleh kosong");
        }
        this.jenisNarkotika = teks.trim();
        return this;
    }

    /**
     * Berat barang bukti (gram) dalam rentang inklusif min <= berat <= max.
     * Catatan: berat 0 berarti "tidak ditemukan" (aturan 2.1), jadi batas bawah > 0
     * otomatis mengecualikan putusan yang beratnya tidak tercatat.
     */
    public PutusanFilter beratBetween(double min, double max) {
        if (min < 0 || min > max) {
            throw new IllegalArgumentException("Rentang berat tidak valid: " + min + ".." + max);
        }
        this.beratAktif = true;
        this.beratMin = min;
        this.beratMax = max;
        return this;
    }

    /** Pasal terbukti mengandung teks ini (tanpa membedakan huruf besar/kecil). */
    public PutusanFilter pasalTerbukti(String teks) {
        if (teks == null || teks.trim().isEmpty()) {
            throw new IllegalArgumentException("Kriteria pasal terbukti tidak boleh kosong");
        }
        this.pasalTerbukti = teks.trim();
        return this;
    }

    /** Vonis (bulan) dalam rentang inklusif min <= vonis <= max. */
    public PutusanFilter vonisBetween(int min, int max) {
        if (min < 0 || min > max) {
            throw new IllegalArgumentException("Rentang vonis tidak valid: " + min + ".." + max);
        }
        this.vonisAktif = true;
        this.vonisMin = min;
        this.vonisMax = max;
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
        if (beratAktif) {
            double berat = p.getBeratBarangBuktiGram();
            if (berat < beratMin || berat > beratMax) {
                return false;
            }
        }
        if (pasalTerbukti != null && !mengandung(p.getPasalTerbukti(), pasalTerbukti)) {
            return false;
        }
        if (vonisAktif) {
            int vonis = p.getVonisBulan();
            if (vonis < vonisMin || vonis > vonisMax) {
                return false;
            }
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