package id.ac.umm.strukturdata;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class PutusanDisplayTest {

    // Data DUMMY fiktif, bukan putusan asli.
    private Putusan buat(String caseId, String jenis, double berat, int vonis) {
        return new Putusan(caseId, "DUMMY/" + caseId, "PN Dummy",
                LocalDate.of(2024, 1, 1), "TDW-DUMMY",
                jenis, berat, null, null,
                0, vonis, 0L,
                null, null, null, null);
    }

    @Test
    void ringkasanMengikutiUrutanList() {
        SinglyLinkedList<Putusan> list = new SinglyLinkedList<>();
        list.add(buat("D1", "Sabu", 0.5, 48));
        list.add(buat("D2", "Ganja", 100.0, 36));
        list.add(buat("D3", "Ekstasi", 2.0, 60));
        String s = PutusanDisplay.ringkasan(list);
        int a = s.indexOf("D1");
        int b = s.indexOf("D2");
        int c = s.indexOf("D3");
        assertTrue(a >= 0 && a < b && b < c);
        assertTrue(s.startsWith("Total: 3 putusan\n"));
    }

    @Test
    void formatSatuBarisSesuaiRingkasan() {
        SinglyLinkedList<Putusan> list = new SinglyLinkedList<>();
        list.add(buat("D1", "Sabu", 0.5, 48));
        assertEquals("Total: 1 putusan\n"
                        + "1. D1 | DUMMY/D1 | PN Dummy | Sabu | 0.5 g | vonis 48 bln\n",
                PutusanDisplay.ringkasan(list));
    }

    @Test
    void listKosongMenampilkanPesanTanpaError() {
        String s = PutusanDisplay.ringkasan(new SinglyLinkedList<>());
        assertTrue(s.contains("Total: 0 putusan"));
        assertTrue(s.contains("Tidak ada putusan"));
    }

    @Test
    void nilaiNullDitampilkanSebagaiStrip() {
        SinglyLinkedList<Putusan> list = new SinglyLinkedList<>();
        list.add(new Putusan(null, null, null, null, null, null, 0,
                null, null, 0, 0, 0L, null, null, null, null));
        assertEquals("Total: 1 putusan\n1. - | - | - | - | 0.0 g | vonis 0 bln\n",
                PutusanDisplay.ringkasan(list));
    }

    @Test
    void ringkasanTidakMengubahList() {
        SinglyLinkedList<Putusan> list = new SinglyLinkedList<>();
        list.add(buat("D1", "Sabu", 0.5, 48));
        list.add(buat("D2", "Ganja", 100.0, 36));
        PutusanDisplay.ringkasan(list);
        assertEquals(2, list.size());
        assertEquals("D1", list.get(0).getCaseId());
        assertEquals("D2", list.get(1).getCaseId());
        list.validateInvariant();
    }

    @Test
    void listNullDitolak() {
        assertThrows(IllegalArgumentException.class, () -> PutusanDisplay.ringkasan(null));
    }

    @Test
    void tampilkanMencetakIsiRingkasan() {
        SinglyLinkedList<Putusan> list = new SinglyLinkedList<>();
        list.add(buat("D1", "Sabu", 0.5, 48));
        PrintStream asli = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(buffer));
            PutusanDisplay.tampilkan(list);
        } finally {
            System.setOut(asli);
        }
        assertEquals(PutusanDisplay.ringkasan(list), buffer.toString());
    }
}