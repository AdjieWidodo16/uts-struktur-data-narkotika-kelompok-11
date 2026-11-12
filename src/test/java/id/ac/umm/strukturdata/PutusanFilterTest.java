package id.ac.umm.strukturdata;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PutusanFilterTest {

    private SinglyLinkedList<Putusan> daftar;

    // Data DUMMY fiktif (bukan putusan asli). Tabel untuk pemeriksaan manual:
    //  id | jenis        | berat | pasalTerbukti        | vonis
    //  D1 | Sabu         |   0.5 | Pasal 112 ayat (1)   |  48
    //  D2 | Sabu         |   5.0 | Pasal 114 ayat (1)   |  72
    //  D3 | Ganja        | 100.0 | Pasal 111 ayat (1)   |  36
    //  D4 | Ekstasi      |   2.0 | Pasal 112 ayat (2)   |  60
    //  D5 | Sabu, Ganja  |  10.0 | Pasal 114 ayat (2)   |  96
    //  D6 | (null)       |   0   | (null)               |   0   <- tidak ditemukan
    @BeforeEach
    void siapkan() {
        daftar = new SinglyLinkedList<>();
        daftar.add(buat("D1", "Sabu", 0.5, "Pasal 112 ayat (1)", 48));
        daftar.add(buat("D2", "Sabu", 5.0, "Pasal 114 ayat (1)", 72));
        daftar.add(buat("D3", "Ganja", 100.0, "Pasal 111 ayat (1)", 36));
        daftar.add(buat("D4", "Ekstasi", 2.0, "Pasal 112 ayat (2)", 60));
        daftar.add(buat("D5", "Sabu, Ganja", 10.0, "Pasal 114 ayat (2)", 96));
        daftar.add(buat("D6", null, 0, null, 0));
    }

    private Putusan buat(String caseId, String jenis, double berat,
                         String pasalTerbukti, int vonis) {
        return new Putusan(caseId, "DUMMY/" + caseId, "PN Dummy",
                LocalDate.of(2024, 1, 1), "TDW-DUMMY",
                jenis, berat,
                null, pasalTerbukti,
                0, vonis, 0L,
                null, null, null, null);
    }

    /** Memastikan isi hasil sama dengan daftar caseId (urut) dan struktur konsisten. */
    private void assertIds(SinglyLinkedList<Putusan> hasil, String... ids) {
        assertEquals(ids.length, hasil.size());
        for (int i = 0; i < ids.length; i++) {
            assertEquals(ids[i], hasil.get(i).getCaseId());
        }
        hasil.validateInvariant();
    }

    @Test
    void filterJenisNarkotikaSabu() {
        assertIds(new PutusanFilter().jenisNarkotika("Sabu").terapkan(daftar),
                "D1", "D2", "D5");
    }

    @Test
    void filterJenisNarkotikaTanpaMembedakanHurufBesarKecil() {
        assertIds(new PutusanFilter().jenisNarkotika("SABU").terapkan(daftar),
                "D1", "D2", "D5");
    }

    @Test
    void filterJenisNarkotikaMembacaDaftarZat() {
        assertIds(new PutusanFilter().jenisNarkotika("ganja").terapkan(daftar),
                "D3", "D5");
    }

    @Test
    void filterJenisNarkotikaTidakAdaMenghasilkanListKosong() {
        SinglyLinkedList<Putusan> hasil = new PutusanFilter().jenisNarkotika("heroin").terapkan(daftar);
        assertTrue(hasil.isEmpty());
        hasil.validateInvariant();
    }

    @Test
    void hasilFilterListBaruDanListAsliTidakBerubah() {
        SinglyLinkedList<Putusan> hasil = new PutusanFilter().jenisNarkotika("sabu").terapkan(daftar);
        assertNotSame(daftar, hasil);
        assertEquals(6, daftar.size());
        String[] urutan = {"D1", "D2", "D3", "D4", "D5", "D6"};
        for (int i = 0; i < urutan.length; i++) {
            assertEquals(urutan[i], daftar.get(i).getCaseId());
        }
        daftar.validateInvariant();
    }

    @Test
    void kriteriaJenisNarkotikaKosongDitolak() {
        assertThrows(IllegalArgumentException.class, () -> new PutusanFilter().jenisNarkotika("  "));
        assertThrows(IllegalArgumentException.class, () -> new PutusanFilter().jenisNarkotika(null));
    }

    @Test
    void listNullDitolak() {
        assertThrows(IllegalArgumentException.class,
                () -> new PutusanFilter().jenisNarkotika("sabu").terapkan(null));
    }

    @Test
    void filterRentangBerat() {
        assertIds(new PutusanFilter().beratBetween(1.0, 10.0).terapkan(daftar),
                "D2", "D4", "D5");
    }

    @Test
    void rentangBeratBatasBawahDanAtasInklusif() {
        assertIds(new PutusanFilter().beratBetween(5.0, 5.0).terapkan(daftar), "D2");
        assertIds(new PutusanFilter().beratBetween(0.5, 0.5).terapkan(daftar), "D1");
        assertIds(new PutusanFilter().beratBetween(10.0, 10.0).terapkan(daftar), "D5");
    }

    @Test
    void rentangBeratDiLuarBatasTidakMenghasilkanApaPun() {
        assertIds(new PutusanFilter().beratBetween(100.1, 500.0).terapkan(daftar));
    }

    @Test
    void beratNolTidakLolosFilterRentang() {
    // Data D6 memiliki berat 0 (tidak ditemukan), sehingga harus dikecualikan.
    assertIds(new PutusanFilter().beratBetween(0.0, 0.0).terapkan(daftar));

    // Rentang yang dimulai dari 0 tetap hanya mengambil data berat yang valid.
    assertIds(new PutusanFilter().beratBetween(0.0, 1000.0).terapkan(daftar),
            "D1", "D2", "D3", "D4", "D5");
}

    @Test
    void rentangBeratTidakValidDitolak() {
        assertThrows(IllegalArgumentException.class, () -> new PutusanFilter().beratBetween(5.0, 1.0));
        assertThrows(IllegalArgumentException.class, () -> new PutusanFilter().beratBetween(-1.0, 1.0));
    }

    @Test
    void filterPasalTerbuktiByNomorPasal() {
        assertIds(new PutusanFilter().pasalTerbukti("112").terapkan(daftar), "D1", "D4");
        assertIds(new PutusanFilter().pasalTerbukti("114").terapkan(daftar), "D2", "D5");
    }

    @Test
    void filterPasalTerbuktiTanpaMembedakanHurufBesarKecil() {
        assertIds(new PutusanFilter().pasalTerbukti("PASAL 112 AYAT (1)").terapkan(daftar), "D1");
    }

    @Test
    void pasalNullTidakPernahLolos() {
        assertIds(new PutusanFilter().pasalTerbukti("pasal").terapkan(daftar),
                "D1", "D2", "D3", "D4", "D5");
    }

    @Test
    void pasalTidakAdaMenghasilkanListKosong() {
        assertIds(new PutusanFilter().pasalTerbukti("999").terapkan(daftar));
    }

    @Test
    void kriteriaPasalKosongDitolak() {
        assertThrows(IllegalArgumentException.class, () -> new PutusanFilter().pasalTerbukti(""));
        assertThrows(IllegalArgumentException.class, () -> new PutusanFilter().pasalTerbukti(null));
    }

    @Test
    void filterRentangVonis() {
        assertIds(new PutusanFilter().vonisBetween(48, 72).terapkan(daftar),
                "D1", "D2", "D4");
    }

    @Test
    void rentangVonisBatasInklusif() {
        assertIds(new PutusanFilter().vonisBetween(36, 36).terapkan(daftar), "D3");
        assertIds(new PutusanFilter().vonisBetween(96, 96).terapkan(daftar), "D5");
    }

    @Test
    void vonisNolTidakLolosFilterRentang() {
    // Data D6 memiliki vonis 0 (tidak ditemukan), sehingga harus dikecualikan.
    assertIds(new PutusanFilter().vonisBetween(0, 0).terapkan(daftar));

    // Rentang yang dimulai dari 0 tetap hanya mengambil data vonis yang valid.
    assertIds(new PutusanFilter().vonisBetween(0, 100).terapkan(daftar),
            "D1", "D2", "D3", "D4", "D5");
}

    @Test
    void rentangVonisDiLuarBatasMenghasilkanListKosong() {
        assertIds(new PutusanFilter().vonisBetween(97, 200).terapkan(daftar));
    }

    @Test
    void rentangVonisTidakValidDitolak() {
        assertThrows(IllegalArgumentException.class, () -> new PutusanFilter().vonisBetween(10, 1));
        assertThrows(IllegalArgumentException.class, () -> new PutusanFilter().vonisBetween(-1, 10));
    }

    @Test
    void filterGabunganDuaKriteria() {
        // sabu: D1,D2,D5 ; vonis 48..96: D1(48),D2(72),D5(96)
        assertIds(new PutusanFilter().jenisNarkotika("sabu").vonisBetween(48, 96).terapkan(daftar),
                "D1", "D2", "D5");
    }

    @Test
    void filterGabunganEmpatKriteriaCocokDenganPemeriksaanManual() {
        // sabu -> D1,D2,D5 ; berat 1..10 -> D2,D5 (D1=0.5 gugur)
        // pasal "114" -> D2,D5 ; vonis 70..100 -> D2(72),D5(96)
        assertIds(new PutusanFilter()
                .jenisNarkotika("sabu")
                .beratBetween(1.0, 10.0)
                .pasalTerbukti("114")
                .vonisBetween(70, 100)
                .terapkan(daftar), "D2", "D5");
    }

    @Test
    void filterGabunganDenganPasalSpesifikHanyaSatuHasil() {
        // "ayat (1)" cocok D1,D2,D3 ; D1 gugur oleh berat, D3 gugur oleh jenis -> D2
        assertIds(new PutusanFilter()
                .jenisNarkotika("sabu")
                .beratBetween(1.0, 10.0)
                .pasalTerbukti("ayat (1)")
                .terapkan(daftar), "D2");
    }

    @Test
    void filterGabunganBertentanganMenghasilkanListKosongYangValid() {
        SinglyLinkedList<Putusan> hasil =
                new PutusanFilter().jenisNarkotika("sabu").vonisBetween(0, 10).terapkan(daftar);
        assertEquals(0, hasil.size());
        assertTrue(hasil.isEmpty());
        hasil.validateInvariant();
    }

    @Test
    void tanpaKriteriaMengembalikanSalinanSeluruhList() {
        SinglyLinkedList<Putusan> hasil = new PutusanFilter().terapkan(daftar);
        assertNotSame(daftar, hasil);
        assertIds(hasil, "D1", "D2", "D3", "D4", "D5", "D6");
    }

    @Test
    void filterBerulangDanGabunganTidakMengubahListAsli() {
        PutusanFilter f = new PutusanFilter().jenisNarkotika("sabu").vonisBetween(48, 96);
        assertIds(f.terapkan(daftar), "D1", "D2", "D5");
        assertIds(f.terapkan(daftar), "D1", "D2", "D5");
        assertEquals(6, daftar.size());
        assertEquals("D1", daftar.get(0).getCaseId());
        assertEquals("D6", daftar.get(5).getCaseId());
        daftar.validateInvariant();
    }

    @Test
    void jumlahKriteriAktifDanToString() {
        PutusanFilter f = new PutusanFilter();
        assertEquals(0, f.jumlahKriteriAktif());
        assertEquals("PutusanFilter{jenisNarkotika=-, berat=-, pasalTerbukti=-, vonis=-}",
                f.toString());

        f.jenisNarkotika("sabu").beratBetween(1.0, 10.0).pasalTerbukti("114").vonisBetween(70, 100);
        assertEquals(4, f.jumlahKriteriAktif());
        assertEquals("PutusanFilter{jenisNarkotika=sabu, berat=1.0..10.0, pasalTerbukti=114, vonis=70..100}",
                f.toString());
    }

}


