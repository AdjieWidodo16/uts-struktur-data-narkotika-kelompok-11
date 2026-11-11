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

    private PutusanSearch() { }
}