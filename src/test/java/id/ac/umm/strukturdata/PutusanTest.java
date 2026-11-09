package id.ac.umm.strukturdata;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class PutusanTest {

    private Putusan buatLengkap() {
        return new Putusan(
                "CASE-001", "123/Pid.Sus/2024/PN Mlg", "PN Malang",
                LocalDate.of(2024, 5, 20), "TDW-001",
                "Sabu", 12.5,
                "Pasal 114 ayat (1)", "Pasal 112 ayat (1)",
                72, 60, 800_000_000L,
                "1 paket plastik berisi kristal bening",
                "Tidak mendukung program pemerintah",
                "Berlaku sopan di persidangan",
                "hlm. 1-3; hlm. 10-12");
    }

    private Putusan buatKosong() {
        return new Putusan(null, null, null, null, null, null, 0,
                null, null, 0, 0, 0L, null, null, null, null);
    }

    @Test
    void semuaAtributFinalDanPrivate() {
        for (java.lang.reflect.Field f : Putusan.class.getDeclaredFields()) {
            int m = f.getModifiers();
            assertTrue(java.lang.reflect.Modifier.isPrivate(m), f.getName() + " harus private");
            assertTrue(java.lang.reflect.Modifier.isFinal(m), f.getName() + " harus final");
        }
    }

    @Test
    void jumlahAtributDanConstructorSesuaiSkema() {
        assertEquals(16, Putusan.class.getDeclaredFields().length);
        assertEquals(1, Putusan.class.getDeclaredConstructors().length);
        assertEquals(16, Putusan.class.getDeclaredConstructors()[0].getParameterCount());
    }

    @Test
    void identitasPerkaraTersimpan() {
        Putusan p = buatLengkap();
        assertEquals("CASE-001", p.getCaseId());
        assertEquals("123/Pid.Sus/2024/PN Mlg", p.getNomorPerkara());
        assertEquals("PN Malang", p.getPengadilan());
        assertEquals(LocalDate.of(2024, 5, 20), p.getTanggalPutusan());
        assertEquals("TDW-001", p.getTerdakwaKode());
    }

    @Test
    void identitasTidakDitemukanNull() {
        Putusan p = buatKosong();
        assertNull(p.getCaseId());
        assertNull(p.getNomorPerkara());
        assertNull(p.getPengadilan());
        assertNull(p.getTanggalPutusan());
        assertNull(p.getTerdakwaKode());
    }


    @Test
    void narkotikaDanPasalTersimpan() {
        Putusan p = buatLengkap();
        assertEquals("Sabu", p.getJenisNarkotika());
        assertEquals(12.5, p.getBeratBarangBuktiGram(), 0.0001);
        assertEquals("Pasal 114 ayat (1)", p.getPasalDakwaan());
        assertEquals("Pasal 112 ayat (1)", p.getPasalTerbukti());
    }

    @Test
    void narkotikaTidakDitemukanNullDanNol() {
        Putusan p = buatKosong();
        assertNull(p.getJenisNarkotika());
        assertEquals(0.0, p.getBeratBarangBuktiGram(), 0.0001);
        assertNull(p.getPasalDakwaan());
        assertNull(p.getPasalTerbukti());
    }


    @Test
    void pidanaTersimpan() {
        Putusan p = buatLengkap();
        assertEquals(72, p.getTuntutanBulan());
        assertEquals(60, p.getVonisBulan());
        assertEquals(800_000_000L, p.getDendaRupiah());
    }

    @Test
    void pidanaTidakDitemukanNol() {
        Putusan p = buatKosong();
        assertEquals(0, p.getTuntutanBulan());
        assertEquals(0, p.getVonisBulan());
        assertEquals(0L, p.getDendaRupiah());
    }

    @Test
    void dendaMenampungNilaiDiAtasBatasInt() {
        Putusan p = new Putusan("C", "N", "P", null, "T", "J", 0,
                "D", "B", 0, 0, 5_000_000_000L, null, null, null, null);
        assertEquals(5_000_000_000L, p.getDendaRupiah());
    }


    @Test
    void barangBuktiKeadaanDanSumberTersimpan() {
        Putusan p = buatLengkap();
        assertEquals("1 paket plastik berisi kristal bening", p.getBarangBukti());
        assertEquals("Tidak mendukung program pemerintah", p.getKeadaanMemberatkan());
        assertEquals("Berlaku sopan di persidangan", p.getKeadaanMeringankan());
        assertEquals("hlm. 1-3; hlm. 10-12", p.getSourcePages());
    }

    @Test
    void barangBuktiKeadaanDanSumberTidakDitemukanNull() {
        Putusan p = buatKosong();
        assertNull(p.getBarangBukti());
        assertNull(p.getKeadaanMemberatkan());
        assertNull(p.getKeadaanMeringankan());
        assertNull(p.getSourcePages());
    }


    @Test
    void toStringHanyaMemuatRingkasan() {
        String s = buatLengkap().toString();
        assertEquals("Putusan{caseId=CASE-001, nomorPerkara=123/Pid.Sus/2024/PN Mlg, vonisBulan=60}", s);
        assertFalse(s.contains("PN Malang"));
        assertFalse(s.contains("TDW-001"));
    }

    @Test
    void toStringAmanUntukNilaiNull() {
        assertEquals("Putusan{caseId=null, nomorPerkara=null, vonisBulan=0}", buatKosong().toString());
    }
}