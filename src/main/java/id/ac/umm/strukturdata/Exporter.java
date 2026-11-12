package id.ac.umm.strukturdata;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

/**
 * Menyimpan hasil/laporan ke file teks atau CSV (hanya java.io).
 * Kegagalan menulis tidak melempar exception: method ekspor mengembalikan false
 * dan alasannya ada di getLastError().
 */
public class Exporter {

    // Urutan kolom mengikuti skema minimum (panduan 2.2).
    static final String CSV_HEADER = "caseId,nomorPerkara,pengadilan,tanggalPutusan,terdakwaKode,"
            + "jenisNarkotika,beratBarangBuktiGram,pasalDakwaan,pasalTerbukti,tuntutanBulan,"
            + "vonisBulan,dendaRupiah,barangBukti,keadaanMemberatkan,keadaanMeringankan,sourcePages";

    private String lastError;

    /** Alasan kegagalan ekspor terakhir; null jika ekspor terakhir berhasil. */
    public String getLastError() {
        return lastError;
    }

    /** Menyusun isi CSV: header, lalu satu baris per putusan sesuai urutan list. */
    public String buatCsv(SinglyLinkedList<Putusan> list) {
        if (list == null) {
            throw new IllegalArgumentException("List putusan tidak boleh null");
        }
        StringBuilder sb = new StringBuilder();
        sb.append(CSV_HEADER).append('\n');
        Node<Putusan> cur = list.getHead();
        while (cur != null) {
            Putusan p = cur.data;
            String[] kolom = {
                    p.getCaseId(), p.getNomorPerkara(), p.getPengadilan(),
                    p.getTanggalPutusan() == null ? null : p.getTanggalPutusan().toString(),
                    p.getTerdakwaKode(), p.getJenisNarkotika(),
                    angka(p.getBeratBarangBuktiGram()),
                    p.getPasalDakwaan(), p.getPasalTerbukti(),
                    String.valueOf(p.getTuntutanBulan()), String.valueOf(p.getVonisBulan()),
                    String.valueOf(p.getDendaRupiah()),
                    p.getBarangBukti(), p.getKeadaanMemberatkan(),
                    p.getKeadaanMeringankan(), p.getSourcePages()
            };
            for (int i = 0; i < kolom.length; i++) {
                if (i > 0) {
                    sb.append(',');
                }
                sb.append(escapeCsv(kolom[i]));
            }
            sb.append('\n');
            cur = cur.next;
        }
        return sb.toString();
    }

    /** @return true jika file berhasil ditulis. */
    public boolean eksporCsv(SinglyLinkedList<Putusan> list, String path) {
        if (list == null) {
            lastError = "List putusan tidak boleh null";
            return false;
        }
        return tulis(path, buatCsv(list));
    }

    /**
     * Laporan teks: ringkasan putusan, agregasi, dan (jika audit tidak null) riwayat audit.
     * Rata-rata vonis dibulatkan 2 desimal.
     */
    public String buatLaporan(SinglyLinkedList<Putusan> list, AuditLog audit) {
        if (list == null) {
            throw new IllegalArgumentException("List putusan tidak boleh null");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("LAPORAN PUTUSAN NARKOTIKA\n\n");
        sb.append(PutusanDisplay.ringkasan(list)).append('\n');

        PutusanAggregator.Ringkasan r = PutusanAggregator.hitung(list);
        sb.append("Agregasi\n");
        sb.append("- Jumlah perkara: ").append(r.getJumlahPerkara()).append('\n');
        sb.append("- Rata-rata vonis (bulan): ")
                .append(Math.round(r.getRataRataVonisBulan() * 100.0) / 100.0).append('\n');
        sb.append("- Total denda (Rp): ").append(r.getTotalDendaRupiah()).append('\n');
        sb.append("- Distribusi jenis narkotika:\n");
        if (r.getDistribusi().isEmpty()) {
            sb.append("  (tidak ada data)\n");
        } else {
            Node<PutusanAggregator.JenisCount> cur = r.getDistribusi().getHead();
            while (cur != null) {
                String jenis = cur.data.getJenis() == null ? "(tidak diketahui)" : cur.data.getJenis();
                sb.append("  * ").append(jenis).append(": ").append(cur.data.getJumlah()).append('\n');
                cur = cur.next;
            }
        }
        if (audit != null) {
            sb.append('\n').append(audit.riwayat());
        }
        return sb.toString();
    }

    public boolean eksporLaporan(SinglyLinkedList<Putusan> list, AuditLog audit, String path) {
        if (list == null) {
            lastError = "List putusan tidak boleh null";
            return false;
        }
        return tulis(path, buatLaporan(list, audit));
    }

    /** Teks hasil pencarian (ditemukan atau tidak, posisi, jumlah langkah). */
    public String buatHasilCari(PutusanSearch.Hasil hasil) {
        if (hasil == null) {
            throw new IllegalArgumentException("Hasil pencarian tidak boleh null");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("HASIL PENCARIAN\n");
        sb.append("Ditemukan: ").append(hasil.isDitemukan() ? "ya" : "tidak").append('\n');
        sb.append("Posisi: ").append(hasil.getPosisi()).append('\n');
        sb.append("Langkah: ").append(hasil.getLangkah()).append('\n');
        if (hasil.isDitemukan()) {
            sb.append("Putusan: ").append(hasil.getPutusan()).append('\n');
        }
        return sb.toString();
    }

    public boolean eksporHasilCari(PutusanSearch.Hasil hasil, String path) {
        if (hasil == null) {
            lastError = "Hasil pencarian tidak boleh null";
            return false;
        }
        return tulis(path, buatHasilCari(hasil));
    }

    /** Null menjadi kosong; teks berkoma/berkutip/baris-baru dibungkus "...", " menjadi "". */
    static String escapeCsv(String nilai) {
        if (nilai == null) {
            return "";
        }
        boolean perluKutip = false;
        for (int i = 0; i < nilai.length(); i++) {
            char c = nilai.charAt(i);
            if (c == ',' || c == '"' || c == '\n' || c == '\r') {
                perluKutip = true;
                break;
            }
        }
        if (!perluKutip) {
            return nilai;
        }
        StringBuilder sb = new StringBuilder("\"");
        for (int i = 0; i < nilai.length(); i++) {
            char c = nilai.charAt(i);
            if (c == '"') {
                sb.append("\"\"");
            } else {
                sb.append(c);
            }
        }
        sb.append('"');
        return sb.toString();
    }

    // Menghindari notasi ilmiah (mis. 1.0E7) agar CSV tetap bisa dibaca ulang.
    private static String angka(double nilai) {
        return BigDecimal.valueOf(nilai).toPlainString();
    }

    private boolean tulis(String path, String isi) {
        if (path == null || path.trim().isEmpty()) {
            lastError = "Path file tidak boleh kosong";
            return false;
        }
        try (BufferedWriter w = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(path), StandardCharsets.UTF_8))) {
            w.write(isi);
            lastError = null;
            return true;
        } catch (IOException e) {
            lastError = "Gagal menulis file '" + path + "': " + e.getMessage();
            return false;
        }
    }
}