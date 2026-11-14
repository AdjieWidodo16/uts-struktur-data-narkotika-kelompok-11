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

}