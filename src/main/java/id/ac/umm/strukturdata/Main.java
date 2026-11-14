package id.ac.umm.strukturdata;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Main {

    static final String PATH_CSV_DEFAULT = "data/putusan-50.csv";

    // Jenis operasi yang belum punya method khusus di AuditLog
    private static final String IMPOR = "IMPOR";
    private static final String TAMPIL = "TAMPIL";
    private static final String AGREGASI = "AGREGASI";
    private static final String URUTAN = "URUTAN";
    private static final String AUDIT = "AUDIT";
    private static final String EKSPOR = "EKSPOR";

    private static final DateTimeFormatter FORMAT_WAKTU =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // indeks = nomor menu (0 = keluar)
    private static final String[] NAMA_MENU = {
            "KELUAR", IMPOR, TAMPIL, AuditLog.TAMBAH, AuditLog.HAPUS, AuditLog.CARI,
            AuditLog.FILTER, AGREGASI, URUTAN, AUDIT, EKSPOR
    };

    private final InputHelper in;
    private final AuditLog audit = new AuditLog();
    private final Exporter exporter = new Exporter();
    private SinglyLinkedList<Putusan> data = new SinglyLinkedList<>();
    private SinglyLinkedList<Putusan> filterTerakhir;     // untuk ekspor
    private PutusanSearch.Hasil cariTerakhir;             // untuk ekspor

    public Main(InputHelper in) {
        this.in = in;
    }

    public static void main(String[] args) {
        new Main(new InputHelper(new Scanner(System.in))).jalankan();
    }

    public void jalankan() {
        try {
            boolean lanjut = true;
            while (lanjut) {
                tampilMenu();
                int pilihan = in.bacaIntDalam("Pilih menu: ", 0, 10);
                lanjut = proses(pilihan);
            }
        } catch (InputHelper.InputBerakhirException e) {
            System.out.println("\nInput berakhir. Program selesai.");
        }
    }

    private void tampilMenu() {
        System.out.println();
        System.out.println("=== MENU PUTUSAN NARKOTIKA ===");
        System.out.println(" 1. Impor CSV");
        System.out.println(" 2. Tampil");
        System.out.println(" 3. Tambah");
        System.out.println(" 4. Hapus");
        System.out.println(" 5. Cari");
        System.out.println(" 6. Filter");
        System.out.println(" 7. Agregasi");
        System.out.println(" 8. Urutan (berdasarkan vonis)");
        System.out.println(" 9. Audit");
        System.out.println("10. Ekspor");
        System.out.println(" 0. Keluar");
        System.out.println("Data saat ini: " + data.size() + " putusan");
    }

    /** @return false jika pengguna memilih keluar. */
    private boolean proses(int pilihan) {
        if (pilihan == 0) {
            System.out.println("Sampai jumpa.");
            return false;
        }
        try {
            switch (pilihan) {
                case 1 -> impor();
                case 2 -> tampil();
                case 3 -> tambah();
                case 4 -> hapus();
                case 5 -> cari();
                case 6 -> filter();
                case 7 -> agregasi();
                case 8 -> urutan();
                case 9 -> menuAudit();
                case 10 -> ekspor();
                default -> System.out.println("Menu belum tersedia.");
            }
        } catch (InputHelper.InputBerakhirException e) {
            throw e;   // ditangani di jalankan()
        } catch (RuntimeException e) {
            System.out.println("Operasi gagal: " + e.getMessage());
            catat(NAMA_MENU[pilihan], "GAGAL: " + e.getMessage());
        }
        return true;
    }

    /** Empat jenis pertama memakai method AuditLog; sisanya diformat sama lewat catat(). */
    private void catat(String jenis, String detail) {
        switch (jenis) {
            case AuditLog.TAMBAH -> audit.catatTambah(detail);
            case AuditLog.HAPUS -> audit.catatHapus(detail);
            case AuditLog.CARI -> audit.catatCari(detail);
            case AuditLog.FILTER -> audit.catatFilter(detail);
            default -> audit.catat("[" + LocalDateTime.now().format(FORMAT_WAKTU) + "] "
                    + jenis + ": " + (detail == null ? "-" : detail));
        }
    }

    private void impor() {
        String path = in.bacaTeksOpsional("Path CSV (Enter = " + PATH_CSV_DEFAULT + "): ");
        if (path == null) {
            path = PATH_CSV_DEFAULT;
        }
        CsvImporter importer = new CsvImporter();
        try {
            SinglyLinkedList<Putusan> hasil = importer.importCsv(Path.of(path));
            hasil.validateInvariant();
            data = hasil;
        } catch (IOException e) {
            System.out.println("Gagal membaca file: " + e.getMessage());
            catat(IMPOR, "GAGAL: path=" + path + ", " + e.getMessage());
            return;
        }
        filterTerakhir = null;
        cariTerakhir = null;

        System.out.println("Dibaca : " + importer.getJumlahDibaca() + " baris");
        System.out.println("Valid  : " + data.size() + " putusan (size = " + data.size() + ")");
        System.out.println("Ditolak: " + importer.getJumlahDitolak());
        Node<String> cur = importer.getErrors().getHead();
        while (cur != null) {
            System.out.println("  - " + cur.data);
            cur = cur.next;
        }
        catat(IMPOR, "path=" + path + ", dibaca=" + importer.getJumlahDibaca()
                + ", valid=" + data.size() + ", ditolak=" + importer.getJumlahDitolak());
    }

    private void tampil() {
        System.out.print(PutusanDisplay.ringkasan(data));
        catat(TAMPIL, "size=" + data.size());
    }

    private void tambah() {
        System.out.println("Tambah putusan (kosongkan kolom opsional jika data tidak ditemukan)");
        String caseId = in.bacaTeksWajib("caseId: ");
        if (adaCaseId(caseId)) {
            System.out.println("caseId sudah ada, penambahan dibatalkan.");
            catat(AuditLog.TAMBAH, "DITOLAK, caseId duplikat: " + caseId);
            return;
        }
        Putusan p = new Putusan(
                caseId,
                in.bacaTeksOpsional("nomorPerkara: "),
                in.bacaTeksOpsional("pengadilan: "),
                in.bacaTanggalOpsional("tanggalPutusan (yyyy-MM-dd): "),
                in.bacaTeksOpsional("terdakwaKode: "),
                in.bacaTeksOpsional("jenisNarkotika: "),
                in.bacaDoubleOpsional("beratBarangBuktiGram: "),
                in.bacaTeksOpsional("pasalDakwaan: "),
                in.bacaTeksOpsional("pasalTerbukti: "),
                in.bacaIntOpsional("tuntutanBulan: "),
                in.bacaIntOpsional("vonisBulan: "),
                in.bacaLongOpsional("dendaRupiah: "),
                in.bacaTeksOpsional("barangBukti: "),
                in.bacaTeksOpsional("keadaanMemberatkan: "),
                in.bacaTeksOpsional("keadaanMeringankan: "),
                in.bacaTeksOpsional("sourcePages: "));

        int posisi = in.bacaIntDalam(
                "Posisi sisip (0 = awal, " + data.size() + " = akhir): ", 0, data.size());
        data.insert(posisi, p);
        data.validateInvariant();
        System.out.println("Ditambahkan di posisi " + posisi + ". Size sekarang " + data.size() + ".");
        catat(AuditLog.TAMBAH, "caseId=" + caseId + ", posisi=" + posisi + ", size=" + data.size());
    }

    private boolean adaCaseId(String id) {
        Node<Putusan> cur = data.getHead();
        while (cur != null) {
            if (id.equals(cur.data.getCaseId())) {
                return true;
            }
            cur = cur.next;
        }
        return false;
    }

    private void hapus() {
        if (data.isEmpty()) {
            System.out.println("Data kosong, tidak ada yang bisa dihapus.");
            catat(AuditLog.HAPUS, "data kosong");
            return;
        }
        int posisi = in.bacaIntDalam(
                "Posisi yang dihapus (0.." + (data.size() - 1) + "): ", 0, data.size() - 1);
        System.out.println("Akan dihapus: " + data.get(posisi));
        if (!in.bacaYa("Lanjutkan? (y/n): ")) {
            System.out.println("Penghapusan dibatalkan.");
            catat(AuditLog.HAPUS, "dibatalkan, posisi=" + posisi);
            return;
        }
        Putusan dihapus = data.remove(posisi);
        data.validateInvariant();
        System.out.println("Dihapus: " + dihapus + ". Size sekarang " + data.size() + ".");
        catat(AuditLog.HAPUS, "posisi=" + posisi + ", caseId=" + dihapus.getCaseId()
                + ", size=" + data.size());
    }

    private void cari() {
        if (data.isEmpty()) {
            System.out.println("Data kosong.");
            catat(AuditLog.CARI, "data kosong");
            return;
        }
        int mode = in.bacaIntDalam("Cari berdasarkan 1) caseId  2) nomorPerkara: ", 1, 2);
        String kunci = in.bacaTeksWajib("Kata kunci: ");
        PutusanSearch.Hasil hasil = (mode == 1)
                ? PutusanSearch.cariByCaseId(data, kunci)
                : PutusanSearch.cariByNomorPerkara(data, kunci);
        cariTerakhir = hasil;
        System.out.print(exporter.buatHasilCari(hasil));
        catat(AuditLog.CARI, (mode == 1 ? "caseId=" : "nomorPerkara=") + kunci
                + ", ditemukan=" + hasil.isDitemukan() + ", posisi=" + hasil.getPosisi()
                + ", langkah=" + hasil.getLangkah());
    }

    private void filter() {
        PutusanFilter f = new PutusanFilter();
        String jenis = in.bacaTeksOpsional("Jenis narkotika (Enter = lewati): ");
        if (jenis != null) {
            f.jenisNarkotika(jenis);
        }
        if (in.bacaYa("Filter rentang berat barang bukti? (y/n): ")) {
            double min = in.bacaDoubleOpsional("  Berat minimum (gram): ");
            double max = in.bacaDoubleOpsional("  Berat maksimum (gram): ");
            f.beratBetween(min, max);
        }
        String pasal = in.bacaTeksOpsional("Pasal terbukti (Enter = lewati): ");
        if (pasal != null) {
            f.pasalTerbukti(pasal);
        }
        if (in.bacaYa("Filter rentang vonis? (y/n): ")) {
            int min = in.bacaIntOpsional("  Vonis minimum (bulan): ");
            int max = in.bacaIntOpsional("  Vonis maksimum (bulan): ");
            f.vonisBetween(min, max);
        }
        if (f.jumlahKriteriAktif() == 0) {
            System.out.println("Tidak ada kriteria aktif, hasilnya seluruh data.");
        }
        SinglyLinkedList<Putusan> hasil = f.terapkan(data);
        filterTerakhir = hasil;
        System.out.println(hasil.size() + " putusan cocok dari " + data.size() + ".");
        System.out.print(PutusanDisplay.ringkasan(hasil));
        catat(AuditLog.FILTER, f + ", hasil=" + hasil.size());
    }

    private void agregasi() {
        PutusanAggregator.Ringkasan r = PutusanAggregator.hitung(data);
        System.out.println("Jumlah perkara        : " + r.getJumlahPerkara());
        System.out.println("Rata-rata vonis (bln) : "
                + Math.round(r.getRataRataVonisBulan() * 100.0) / 100.0);
        System.out.println("Total denda (Rp)      : " + r.getTotalDendaRupiah());
        System.out.println("Distribusi jenis narkotika:");
        if (r.getDistribusi().isEmpty()) {
            System.out.println("  (tidak ada data)");
        }
        Node<PutusanAggregator.JenisCount> cur = r.getDistribusi().getHead();
        while (cur != null) {
            String jenis = cur.data.getJenis() == null ? "(tidak diketahui)" : cur.data.getJenis();
            System.out.println("  - " + jenis + ": " + cur.data.getJumlah());
            cur = cur.next;
        }
        catat(AGREGASI, "perkara=" + r.getJumlahPerkara() + ", jenis=" + r.getBanyakJenis());
    }

    private void urutan() {
        boolean naik = in.bacaIntDalam("Urutkan vonis 1) naik  2) turun: ", 1, 2) == 1;
        SinglyLinkedList<Putusan> terurut = PutusanSorter.urutkanByVonis(data, naik);
        terurut.validateInvariant();
        data.validateInvariant();   // list asli tetap utuh
        System.out.print(PutusanDisplay.ringkasan(terurut));
        System.out.println("(List asli tidak diubah; size asli = " + data.size() + ")");
        catat(URUTAN, "vonis " + (naik ? "naik" : "turun") + ", n=" + terurut.size());
    }

    private void menuAudit() {
        int mode = in.bacaIntDalam("1) Tampilkan riwayat  2) Cari entri di riwayat: ", 1, 2);
        if (mode == 1) {
            audit.tampilkanRiwayat();
            catat(AUDIT, "tampilkan riwayat");
        } else {
            cariDiAudit();
        }
    }

    private void cariDiAudit() {
        if (audit.isEmpty()) {
            System.out.println("Riwayat masih kosong.");
            return;
        }
        String entri = in.bacaTeksWajib(
                "Tempel entri lengkap dari riwayat (tanpa nomor urut): ");
        String terbaruSebelum = audit.terbaru();
        int jumlahSebelum = audit.jumlah();

        int posisi = audit.posisiDariTerbaru(entri);
        int langkah = audit.getLastSearchSteps();

        System.out.println(posisi == -1
                ? "Entri tidak ditemukan."
                : "Entri ditemukan pada kedalaman " + posisi + " dari puncak.");
        System.out.println("Langkah pencarian: " + langkah);

        boolean utuh = terbaruSebelum.equals(audit.terbaru()) && jumlahSebelum == audit.jumlah();
        audit.validateInvariant();
        System.out.println("Urutan stack tidak berubah: " + (utuh ? "ya" : "TIDAK"));
        catat(AUDIT, "cari entri, posisi=" + posisi + ", langkah=" + langkah);
    }

    private void ekspor() {
        System.out.println("1) CSV data saat ini");
        System.out.println("2) CSV hasil filter terakhir");
        System.out.println("3) Laporan (agregasi + riwayat audit)");
        System.out.println("4) Hasil pencarian terakhir");
        int mode = in.bacaIntDalam("Pilih: ", 1, 4);

        if (mode == 2 && filterTerakhir == null) {
            System.out.println("Belum ada hasil filter. Jalankan menu Filter dulu.");
            catat(EKSPOR, "GAGAL: belum ada hasil filter");
            return;
        }
        if (mode == 4 && cariTerakhir == null) {
            System.out.println("Belum ada hasil pencarian. Jalankan menu Cari dulu.");
            catat(EKSPOR, "GAGAL: belum ada hasil pencarian");
            return;
        }

        String bawaan = switch (mode) {
            case 1 -> "docs/demo/data.csv";
            case 2 -> "docs/demo/hasil-filter.csv";
            case 3 -> "docs/demo/laporan.txt";
            default -> "docs/demo/hasil-cari.txt";
        };
        String path = in.bacaTeksOpsional("Path file (Enter = " + bawaan + "): ");
        if (path == null) {
            path = bawaan;
        }

        boolean ok = switch (mode) {
            case 1 -> exporter.eksporCsv(data, path);
            case 2 -> exporter.eksporCsv(filterTerakhir, path);
            case 3 -> exporter.eksporLaporan(data, audit, path);
            default -> exporter.eksporHasilCari(cariTerakhir, path);
        };
        if (ok) {
            System.out.println("Berhasil disimpan ke " + path);
            catat(EKSPOR, "mode=" + mode + ", path=" + path);
        } else {
            System.out.println("Gagal: " + exporter.getLastError());
            catat(EKSPOR, "GAGAL: " + exporter.getLastError());
        }
    }

}