package id.ac.umm.strukturdata;

public final class PutusanAggregator {

    private PutusanAggregator() { }

    /** Satu baris distribusi: jenis narkotika dan jumlah perkaranya. jenis null = tidak diketahui. */
    public static final class JenisCount {
        private final String jenis;
        private int jumlah;

        JenisCount(String jenis, int jumlah) {
            this.jenis = jenis;
            this.jumlah = jumlah;
        }

        public String getJenis() { return jenis; }
        public int getJumlah() { return jumlah; }

        void tambah() {
            jumlah++;
        }

        @Override
        public String toString() {
            return "JenisCount{jenis=" + jenis + ", jumlah=" + jumlah + "}";
        }
    }
}