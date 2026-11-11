package id.ac.umm.strukturdata;

public class PutusanSearch {

    /**
     * Hasil pencarian.
     * putusan: null jika tidak ditemukan.
     * posisi : indeks 0-based, -1 jika tidak ditemukan.
     * langkah: jumlah node yang dibandingkan.
     */
    public static final class Hasil {
        private final Putusan putusan;
        private final int posisi;
        private final int langkah;

        public Hasil(Putusan putusan, int posisi, int langkah) {
            this.putusan = putusan;
            this.posisi = posisi;
            this.langkah = langkah;
        }

        public Putusan getPutusan() { return putusan; }
        public int getPosisi() { return posisi; }
        public int getLangkah() { return langkah; }
        public boolean isDitemukan() { return putusan != null; }

        @Override
        public String toString() {
            return "Hasil{ditemukan=" + isDitemukan()
                    + ", posisi=" + posisi + ", langkah=" + langkah + "}";
        }
    }

    public static Hasil cariByCaseId(SinglyLinkedList<Putusan> list, String caseId) {
        int langkah = 0;
        Node<Putusan> cur = list.getHead();
        while (cur != null) {
            langkah++;                                  // satu node dibandingkan
            if (sama(cur.data.getCaseId(), caseId)) {
                return new Hasil(cur.data, langkah - 1, langkah);
            }
            cur = cur.next;
        }
        return new Hasil(null, -1, langkah);            // tidak ketemu: langkah = jumlah node
    }

    private static boolean sama(String nilai, String kunci) {
        return nilai != null && nilai.equals(kunci);    // aman untuk nilai null
    }

    public static Hasil cariByNomorPerkara(SinglyLinkedList<Putusan> list, String nomorPerkara) {
        int langkah = 0;
        Node<Putusan> cur = list.getHead();
        while (cur != null) {
            langkah++;
            if (sama(cur.data.getNomorPerkara(), nomorPerkara)) {
                return new Hasil(cur.data, langkah - 1, langkah);
            }
            cur = cur.next;
        }
        return new Hasil(null, -1, langkah);
    }

    private PutusanSearch() { }
}