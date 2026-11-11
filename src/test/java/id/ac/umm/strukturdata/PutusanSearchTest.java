package id.ac.umm.strukturdata;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PutusanSearchTest {

    @Test
    void hasilTidakDitemukanMenyimpanPosisiDanLangkah() {
        PutusanSearch.Hasil h = new PutusanSearch.Hasil(null, -1, 3);
        assertFalse(h.isDitemukan());
        assertNull(h.getPutusan());
        assertEquals(-1, h.getPosisi());
        assertEquals(3, h.getLangkah());
    }

    private SinglyLinkedList<Putusan> listIsi3() {
        SinglyLinkedList<Putusan> list = new SinglyLinkedList<>();
        list.addLast(buat("C1", "1/Pid.Sus/2024/PN A"));
        list.addLast(buat("C2", "2/Pid.Sus/2024/PN B"));
        list.addLast(buat("C3", "3/Pid.Sus/2024/PN C"));
        return list;
    }

    @Test
    void caseIdDiAwalLangkahSatu() {
        PutusanSearch.Hasil h = PutusanSearch.cariByCaseId(listIsi3(), "C1");
        assertTrue(h.isDitemukan());
        assertEquals("C1", h.getPutusan().getCaseId());
        assertEquals(0, h.getPosisi());
        assertEquals(1, h.getLangkah());
    }

    @Test
    void caseIdDiAkhirLangkahSamaDenganJumlahNode() {
        PutusanSearch.Hasil h = PutusanSearch.cariByCaseId(listIsi3(), "C3");
        assertTrue(h.isDitemukan());
        assertEquals(2, h.getPosisi());
        assertEquals(3, h.getLangkah());
    }

    @Test
    void caseIdTidakAdaLangkahSamaDenganJumlahNode() {
        PutusanSearch.Hasil h = PutusanSearch.cariByCaseId(listIsi3(), "XX");
        assertFalse(h.isDitemukan());
        assertNull(h.getPutusan());
        assertEquals(-1, h.getPosisi());
        assertEquals(3, h.getLangkah());
    }

    @Test
    void caseIdPadaListKosongLangkahNol() {
        PutusanSearch.Hasil h = PutusanSearch.cariByCaseId(new SinglyLinkedList<>(), "C1");
        assertFalse(h.isDitemukan());
        assertEquals(-1, h.getPosisi());
        assertEquals(0, h.getLangkah());
    }

    /** Putusan minimal: hanya caseId & nomorPerkara bermakna; sisanya null (teks/tanggal) atau 0 (angka). */
    private Putusan buat(String caseId, String nomorPerkara) {
        return new Putusan(
                caseId, nomorPerkara,
                null,   // pengadilan
                null,   // tanggalPutusan
                null,   // terdakwaKode
                null,   // jenisNarkotika
                0,      // beratBarangBuktiGram
                null,   // pasalDakwaan
                null,   // pasalTerbukti
                0,      // tuntutanBulan
                0,      // vonisBulan
                0L,     // dendaRupiah
                null,   // barangBukti
                null,   // keadaanMemberatkan
                null,   // keadaanMeringankan
                null    // sourcePages
        );
    }

}