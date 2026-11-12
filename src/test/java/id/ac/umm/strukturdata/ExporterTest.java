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
}