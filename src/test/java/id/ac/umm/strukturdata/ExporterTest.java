package id.ac.umm.strukturdata;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ExporterTest {

    @TempDir
    Path folder;

    // Data DUMMY fiktif, bukan putusan asli. Kode terdakwa mengikuti format ADR-003 (T001).
    private Putusan dummy(String id, String jenis, double berat, int vonis, long denda) {
        return new Putusan(id, "DUMMY/" + id, "PN Dummy",
                LocalDate.of(2024, 1, 1), "T001",
                jenis, berat, "Pasal 114", "Pasal 112",
                72, vonis, denda,
                null, null, null, "hlm. 1");
    }

    private Putusan kosong() {
        return new Putusan(null, null, null, null, null, null, 0,
                null, null, 0, 0, 0L, null, null, null, null);
    }

    @Test
    void headerCsvMemuatEnamBelasKolom() {
        assertEquals(16, Exporter.CSV_HEADER.split(",").length);
    }

    @Test
    void csvListKosongHanyaBerisiHeader() {
        assertEquals(Exporter.CSV_HEADER + "\n", new Exporter().buatCsv(new SinglyLinkedList<>()));
    }

    @Test
    void csvSatuBarisSesuaiUrutanKolom() {
        SinglyLinkedList<Putusan> list = new SinglyLinkedList<>();
        list.add(dummy("D1", "Sabu", 0.5, 48, 1_000_000L));
        assertEquals(Exporter.CSV_HEADER + "\n"
                        + "D1,DUMMY/D1,PN Dummy,2024-01-01,T001,Sabu,0.5,Pasal 114,Pasal 112,"
                        + "72,48,1000000,,,,hlm. 1\n",
                new Exporter().buatCsv(list));
    }

    @Test
    void csvMengikutiUrutanList() {
        SinglyLinkedList<Putusan> list = new SinglyLinkedList<>();
        list.add(dummy("D1", "Sabu", 1, 1, 0));
        list.add(dummy("D2", "Ganja", 1, 1, 0));
        String[] baris = new Exporter().buatCsv(list).split("\n");
        assertEquals(3, baris.length);
        assertTrue(baris[1].startsWith("D1,"));
        assertTrue(baris[2].startsWith("D2,"));
    }

    @Test
    void teksBerkomaDanBerkutipDiEscape() {
        assertEquals("\"paket, plastik\"", Exporter.escapeCsv("paket, plastik"));
        assertEquals("\"Berat \"\"tinggi\"\"\"", Exporter.escapeCsv("Berat \"tinggi\""));
        assertEquals("\"baris1\nbaris2\"", Exporter.escapeCsv("baris1\nbaris2"));
        assertEquals("biasa", Exporter.escapeCsv("biasa"));
        assertEquals("", Exporter.escapeCsv(null));
    }

    @Test
    void nilaiNullDanNolDiTulisKosongDanNol() {
        SinglyLinkedList<Putusan> list = new SinglyLinkedList<>();
        list.add(kosong());
        String baris = new Exporter().buatCsv(list).split("\n")[1];
        assertEquals(",,,,,,0.0,,,0,0,0,,,,", baris);
    }

    @Test
    void beratBesarTidakMemakaiNotasiIlmiah() {
        SinglyLinkedList<Putusan> list = new SinglyLinkedList<>();
        list.add(dummy("D1", "Ganja", 12_500_000.0, 1, 0));
        assertTrue(new Exporter().buatCsv(list).contains(",12500000,"));
    }

    @Test
    void formatAdr003MultiNilaiDanSourcePagesTidakDikutip() {
        SinglyLinkedList<Putusan> list = new SinglyLinkedList<>();
        list.add(new Putusan("P001", "123/Pid.Sus/2024/PN Mlg", "PN Malang",
                LocalDate.of(2024, 5, 20), "T001", "Sabu;Ganja", 10.5,
                "Pasal 114", "Pasal 112", 72, 60, 1_000_000_000L,
                null, null, null,
                "identitas:1; barangBukti:6; tuntutan:3; pertimbangan:25-26; amar:30"));
        String baris = new Exporter().buatCsv(list).split("\n")[1];
        assertEquals("P001,123/Pid.Sus/2024/PN Mlg,PN Malang,2024-05-20,T001,Sabu;Ganja,10.5,"
                + "Pasal 114,Pasal 112,72,60,1000000000,,,,"
                + "identitas:1; barangBukti:6; tuntutan:3; pertimbangan:25-26; amar:30", baris);
    }

    @Test
    void eksporCsvMenulisFileDanListTidakBerubah() throws IOException {
        SinglyLinkedList<Putusan> list = new SinglyLinkedList<>();
        list.add(dummy("D1", "Sabu", 0.5, 48, 0));
        list.add(dummy("D2", "Ganja", 100, 36, 0));
        Path file = folder.resolve("hasil.csv");
        Exporter ex = new Exporter();
        assertTrue(ex.eksporCsv(list, file.toString()));
        assertNull(ex.getLastError());
        assertEquals(ex.buatCsv(list), Files.readString(file));
        assertEquals(2, list.size());
        assertEquals("D1", list.get(0).getCaseId());
        list.validateInvariant();
    }

    @Test
    void listNullDiBuatCsvDitolak() {
        assertThrows(IllegalArgumentException.class, () -> new Exporter().buatCsv(null));
    }

    @Test
    void laporanMemuatRingkasanAgregasiDanDistribusi() {
        SinglyLinkedList<Putusan> list = new SinglyLinkedList<>();
        list.add(dummy("D1", "Sabu", 0.5, 48, 1_000_000L));
        list.add(dummy("D2", "Ganja", 100, 72, 2_000_000L));
        String s = new Exporter().buatLaporan(list, null);
        assertTrue(s.startsWith("LAPORAN PUTUSAN NARKOTIKA\n"));
        assertTrue(s.contains("Total: 2 putusan"));
        assertTrue(s.contains("- Jumlah perkara: 2\n"));
        assertTrue(s.contains("- Rata-rata vonis (bulan): 60.0\n"));
        assertTrue(s.contains("- Total denda (Rp): 3000000\n"));
        assertTrue(s.contains("  * Sabu: 1\n"));
        assertTrue(s.contains("  * Ganja: 1\n"));
        assertFalse(s.contains("Riwayat audit"));
    }

    @Test
    void jenisGabunganDenganTitikKomaDihitungSebagaiJenisTersendiri() {
        SinglyLinkedList<Putusan> list = new SinglyLinkedList<>();
        list.add(dummy("D1", "Sabu;Ganja", 1, 1, 0));
        list.add(dummy("D2", "Sabu", 1, 1, 0));
        String s = new Exporter().buatLaporan(list, null);
        assertTrue(s.contains("  * Sabu;Ganja: 1\n"));
        assertTrue(s.contains("  * Sabu: 1\n"));
    }

    @Test
    void laporanDenganAuditMemuatRiwayatDanUrutanStackTetap() {
        SinglyLinkedList<Putusan> list = new SinglyLinkedList<>();
        list.add(dummy("D1", "Sabu", 0.5, 48, 0));
        AuditLog audit = new AuditLog();
        audit.catatTambah("D1");
        audit.catatCari("D1");
        String terbaruSebelum = audit.terbaru();
        String s = new Exporter().buatLaporan(list, audit);
        assertTrue(s.contains("Riwayat audit: 2 entri"));
        assertTrue(s.contains("] CARI: D1"));
        assertEquals(2, audit.jumlah());
        assertEquals(terbaruSebelum, audit.terbaru());
        audit.validateInvariant();
    }

    @Test
    void laporanListKosongAmanDanJenisNullDilabeli() {
        String kosongS = new Exporter().buatLaporan(new SinglyLinkedList<>(), null);
        assertTrue(kosongS.contains("- Jumlah perkara: 0\n"));
        assertTrue(kosongS.contains("(tidak ada data)"));

        SinglyLinkedList<Putusan> list = new SinglyLinkedList<>();
        list.add(kosong());
        assertTrue(new Exporter().buatLaporan(list, null).contains("(tidak diketahui): 1"));
    }

    @Test
    void eksporLaporanMenulisFile() throws IOException {
        SinglyLinkedList<Putusan> list = new SinglyLinkedList<>();
        list.add(dummy("D1", "Sabu", 0.5, 48, 0));
        Path file = folder.resolve("laporan.txt");
        Exporter ex = new Exporter();
        assertTrue(ex.eksporLaporan(list, null, file.toString()));
        assertEquals(ex.buatLaporan(list, null), Files.readString(file));
    }

    @Test
    void hasilCariDitemukanDiEkspor() throws IOException {
        SinglyLinkedList<Putusan> list = new SinglyLinkedList<>();
        list.add(dummy("D1", "Sabu", 0.5, 48, 0));
        list.add(dummy("D2", "Ganja", 100, 72, 0));
        PutusanSearch.Hasil hasil = PutusanSearch.cariByCaseId(list, "D2");
        Path file = folder.resolve("cari.txt");
        Exporter ex = new Exporter();
        assertTrue(ex.eksporHasilCari(hasil, file.toString()));
        String isi = Files.readString(file);
        assertTrue(isi.contains("Ditemukan: ya"));
        assertTrue(isi.contains("Posisi: 1"));
        assertTrue(isi.contains("Langkah: 2"));
        assertTrue(isi.contains("caseId=D2"));
    }

    @Test
    void hasilCariTidakDitemukanTanpaBarisPutusan() {
        SinglyLinkedList<Putusan> list = new SinglyLinkedList<>();
        list.add(dummy("D1", "Sabu", 0.5, 48, 0));
        String s = new Exporter().buatHasilCari(PutusanSearch.cariByCaseId(list, "ZZZ"));
        assertTrue(s.contains("Ditemukan: tidak"));
        assertTrue(s.contains("Posisi: -1"));
        assertFalse(s.contains("Putusan:"));
    }

    @Test
    void hasilCariNullDitolak() {
        assertThrows(IllegalArgumentException.class, () -> new Exporter().buatHasilCari(null));
    }

    @Test
    void folderTujuanTidakAdaMengembalikanFalseDenganPesan() {
        SinglyLinkedList<Putusan> list = new SinglyLinkedList<>();
        list.add(dummy("D1", "Sabu", 0.5, 48, 0));
        Exporter ex = new Exporter();
        Path salah = folder.resolve("tidak-ada").resolve("sub").resolve("out.csv");
        assertFalse(ex.eksporCsv(list, salah.toString()));
        assertNotNull(ex.getLastError());
        assertTrue(ex.getLastError().contains("Gagal menulis file"));
        assertFalse(Files.exists(salah));
    }

    @Test
    void pathBerupaFolderMengembalikanFalse() {
        Exporter ex = new Exporter();
        assertFalse(ex.eksporLaporan(new SinglyLinkedList<>(), null, folder.toString()));
        assertNotNull(ex.getLastError());
    }

    @Test
    void pathNullAtauKosongMengembalikanFalse() {
        SinglyLinkedList<Putusan> list = new SinglyLinkedList<>();
        Exporter ex = new Exporter();
        assertFalse(ex.eksporCsv(list, null));
        assertEquals("Path file tidak boleh kosong", ex.getLastError());
        assertFalse(ex.eksporCsv(list, "   "));
        assertEquals("Path file tidak boleh kosong", ex.getLastError());
    }

    @Test
    void listAtauHasilNullMengembalikanFalseTanpaCrash() {
        Exporter ex = new Exporter();
        String path = folder.resolve("x.txt").toString();
        assertFalse(ex.eksporCsv(null, path));
        assertFalse(ex.eksporLaporan(null, null, path));
        assertFalse(ex.eksporHasilCari(null, path));
        assertFalse(Files.exists(Path.of(path)));
    }

    @Test
    void eksporBerhasilSetelahGagalMenghapusPesanError() {
        SinglyLinkedList<Putusan> list = new SinglyLinkedList<>();
        Exporter ex = new Exporter();
        assertFalse(ex.eksporCsv(list, folder.resolve("a").resolve("b.csv").toString()));
        assertNotNull(ex.getLastError());
        assertTrue(ex.eksporCsv(list, folder.resolve("ok.csv").toString()));
        assertNull(ex.getLastError());
    }

    @Test
    void kegagalanEksporTidakMengubahListAtauAudit() {
        SinglyLinkedList<Putusan> list = new SinglyLinkedList<>();
        list.add(dummy("D1", "Sabu", 0.5, 48, 0));
        AuditLog audit = new AuditLog();
        audit.catatTambah("D1");
        new Exporter().eksporLaporan(list, audit, folder.resolve("x").resolve("y.txt").toString());
        assertEquals(1, list.size());
        assertEquals(1, audit.jumlah());
        list.validateInvariant();
        audit.validateInvariant();
    }

}