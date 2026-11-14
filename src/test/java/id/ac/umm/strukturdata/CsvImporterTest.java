package id.ac.umm.strukturdata;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.assertNull;

class CsvImporterTest {

    private static final String HEADER =
            "caseId,nomorPerkara,pengadilan,tanggalPutusan,terdakwaKode,jenisNarkotika,"
                    + "beratBarangBuktiGram,pasalDakwaan,pasalTerbukti,tuntutanBulan,vonisBulan,"
                    + "dendaRupiah,barangBukti,keadaanMemberatkan,keadaanMeringankan,sourcePages";

    private static final String VALID_1 =
            "P001,854/Pid.Sus/2025/PN Btm,Pengadilan Negeri Batam,2025-12-01,T001,sabu,1.17,"
                    + "Pasal 114 ayat (1),Pasal 114 ayat (1) UU 35/2009,60,72,2125000000,"
                    + "\"6 paket sabu netto 1,17 g; timbangan digital\",Meresahkan,Belum pernah dihukum,"
                    + "\"identitas:1; tuntutan:2\"";

    private static final String VALID_2 =
            "P002,1123/Pid.Sus/2025/PN Btm,Pengadilan Negeri Batam,2026-03-05,T002,sabu,3.63,"
                    + "Pasal 114 ayat (1),Pasal 114 ayat (1) UU 35/2009,78,72,1000000000,"
                    + "\"12 paket sabu netto 3,63 g\",Gencar pemberantasan,Kooperatif,\"identitas:1\"";

    @TempDir
    Path tmp;

    private Path tulis(String... baris) throws IOException {
        Path file = tmp.resolve("putusan-test.csv");
        Files.write(file, String.join("\n", baris).getBytes(StandardCharsets.UTF_8));
        return file;
    }

    @Test
    void dataValid_semuaRecordMasukDanKutipTerbaca() throws IOException {
        CsvImporter importer = new CsvImporter();
        SinglyLinkedList<Putusan> hasil = importer.importCsv(tulis(HEADER, VALID_1, VALID_2));

        assertEquals(2, hasil.size());
        assertEquals(0, importer.getJumlahDitolak());
        hasil.validateInvariant();

        Putusan p = hasil.get(0);
        assertEquals("P001", p.getCaseId());
        assertEquals(LocalDate.of(2025, 12, 1), p.getTanggalPutusan());
        assertEquals(1.17, p.getBeratBarangBuktiGram(), 0.0001);
        assertEquals(2125000000L, p.getDendaRupiah());
        assertEquals("6 paket sabu netto 1,17 g; timbangan digital", p.getBarangBukti());
    }

    @Test
    void kolomKurang_ditolakDanImporBerlanjut() throws IOException {
        CsvImporter importer = new CsvImporter();
        SinglyLinkedList<Putusan> hasil =
                importer.importCsv(tulis(HEADER, VALID_1, "P099,hanya,tiga", VALID_2));

        assertEquals(2, hasil.size());
        assertEquals(1, importer.getJumlahDitolak());
        String pesan = importer.getErrors().get(0);
        assertTrue(pesan.contains("Baris 3"));
        assertTrue(pesan.contains("jumlah kolom"));
        hasil.validateInvariant();
        assertEquals("P002", hasil.get(1).getCaseId());
    }

    @Test
    void barisKosong_ditolakDanImporBerlanjut() throws IOException {
        CsvImporter importer = new CsvImporter();
        SinglyLinkedList<Putusan> hasil =
                importer.importCsv(tulis(HEADER, VALID_1, "", VALID_2));

        assertEquals(2, hasil.size());
        assertEquals(1, importer.getJumlahDitolak());
        String pesan = importer.getErrors().get(0);
        assertTrue(pesan.contains("Baris 3"));
        assertTrue(pesan.contains("kosong"));
        hasil.validateInvariant();
    }

    @Test
    void angkaBukanAngka_ditolak() throws IOException {
        CsvImporter importer = new CsvImporter();
        String rusak = VALID_1.replace(",72,", ",abc,");
        SinglyLinkedList<Putusan> hasil = importer.importCsv(tulis(HEADER, rusak, VALID_2));

        assertEquals(1, hasil.size());
        assertEquals(1, importer.getJumlahDitolak());
        String pesan = importer.getErrors().get(0);
        assertTrue(pesan.contains("Baris 2"));
        assertTrue(pesan.contains("vonisBulan"));
        hasil.validateInvariant();
        assertEquals("P002", hasil.get(0).getCaseId());
    }

    @Test
    void tanggalSalah_ditolak() throws IOException {
        CsvImporter importer = new CsvImporter();
        String rusak = VALID_1.replace("2025-12-01", "12/1/2025"); // format lama dari Excel
        SinglyLinkedList<Putusan> hasil = importer.importCsv(tulis(HEADER, rusak, VALID_2));

        assertEquals(1, hasil.size());
        assertEquals(1, importer.getJumlahDitolak());
        String pesan = importer.getErrors().get(0);
        assertTrue(pesan.contains("Baris 2"));
        assertTrue(pesan.contains("tanggalPutusan"));
        hasil.validateInvariant();
    }

    @Test
    void nilaiKosongDanNullLiteral_menjadiNolAtauNull() throws IOException {
        String baris = VALID_1
                .replace(",Belum pernah dihukum,", ",null,")  // teks "null" -> null
                .replace(",60,72,", ",,null,");               // angka kosong dan "null" -> 0

        CsvImporter importer = new CsvImporter();
        SinglyLinkedList<Putusan> hasil = importer.importCsv(tulis(HEADER, baris));

        assertEquals(1, hasil.size());
        Putusan p = hasil.get(0);
        assertNull(p.getKeadaanMeringankan());
        assertEquals(0, p.getTuntutanBulan());
        assertEquals(0, p.getVonisBulan());
    }

    // Ubah ke 50 setelah data P026-P050 masuk ke data/putusan-50.csv.
    private static final int JUMLAH_RECORD_CSV = 25;

    private static String ringkasError(CsvImporter importer) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < importer.getErrors().size(); i++) {
            sb.append(importer.getErrors().get(i)).append("; ");
        }
        return sb.toString();
    }

    @Test
    void fileAsli_semuaRecordTerimpor() throws IOException {
        CsvImporter importer = new CsvImporter();
        SinglyLinkedList<Putusan> hasil = importer.importCsv(Path.of("data/putusan-50.csv"));

        assertEquals(0, importer.getJumlahDitolak(), ringkasError(importer));
        assertEquals(JUMLAH_RECORD_CSV, hasil.size());
        hasil.validateInvariant();

        assertEquals(LocalDate.of(2026, 9, 30), hasil.get(2).getTanggalPutusan()); // P003
        assertEquals("P018", hasil.get(17).getCaseId());
        assertNull(hasil.get(17).getKeadaanMeringankan());                         // null literal
    }

}