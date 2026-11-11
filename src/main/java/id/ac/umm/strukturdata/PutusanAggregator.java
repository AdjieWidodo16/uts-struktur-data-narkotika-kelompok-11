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

    /** Hasil agregasi (immutable). */
    public static final class Ringkasan {
        private final int jumlahPerkara;
        private final double rataRataVonisBulan;
        private final long totalDendaRupiah;
        private final SinglyLinkedList<JenisCount> distribusi;

        Ringkasan(int jumlahPerkara, double rataRataVonisBulan, long totalDendaRupiah,
                  SinglyLinkedList<JenisCount> distribusi) {
            this.jumlahPerkara = jumlahPerkara;
            this.rataRataVonisBulan = rataRataVonisBulan;
            this.totalDendaRupiah = totalDendaRupiah;
            this.distribusi = distribusi;
        }

        public int getJumlahPerkara() { return jumlahPerkara; }
        public double getRataRataVonisBulan() { return rataRataVonisBulan; }
        public long getTotalDendaRupiah() { return totalDendaRupiah; }
        public SinglyLinkedList<JenisCount> getDistribusi() { return distribusi; }

        /** Banyaknya jenis narkotika yang berbeda. */
        public int getBanyakJenis() {
            return distribusi.size();
        }

        /** Jumlah perkara untuk satu jenis; 0 jika jenis tidak ada. */
        public int jumlahJenis(String jenis) {
            Node<JenisCount> cur = distribusi.getHead();
            while (cur != null) {
                if (samaJenis(cur.data.getJenis(), jenis)) {
                    return cur.data.getJumlah();
                }
                cur = cur.next;
            }
            return 0;
        }
    }

    public static Ringkasan hitung(SinglyLinkedList<Putusan> list) {
        int jumlah = list.size();
        long totalVonis = 0;
        long totalDenda = 0;
        SinglyLinkedList<JenisCount> distribusi = new SinglyLinkedList<>();

        Node<Putusan> cur = list.getHead();
        while (cur != null) {
            totalVonis += cur.data.getVonisBulan();
            totalDenda += cur.data.getDendaRupiah();
            catat(distribusi, cur.data.getJenisNarkotika());
            cur = cur.next;
        }

        double rata = (jumlah == 0) ? 0.0 : (double) totalVonis / jumlah;   // hindari bagi nol
        return new Ringkasan(jumlah, rata, totalDenda, distribusi);
    }

    // tambah 1 pada jenis yang sudah ada, atau buat baris baru di akhir (urutan = kemunculan pertama)
    private static void catat(SinglyLinkedList<JenisCount> distribusi, String jenis) {
        Node<JenisCount> cur = distribusi.getHead();
        while (cur != null) {
            if (samaJenis(cur.data.getJenis(), jenis)) {
                cur.data.tambah();
                return;
            }
            cur = cur.next;
        }
        distribusi.addLast(new JenisCount(jenis, 1));
    }

    // perbandingan aman null tanpa java.util.Objects
    private static boolean samaJenis(String a, String b) {
        return (a == null) ? b == null : a.equals(b);
    }

}