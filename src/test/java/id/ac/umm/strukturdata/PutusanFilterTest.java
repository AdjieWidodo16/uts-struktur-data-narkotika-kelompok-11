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
}