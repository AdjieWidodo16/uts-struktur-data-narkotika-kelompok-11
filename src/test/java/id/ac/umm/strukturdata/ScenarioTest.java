package id.ac.umm.strukturdata;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Tujuh skenario wajib Panduan UTS 4.1.
 * Area Adjie  : skenario 2, 3, 4  (bagian "AREA ADJIE")
 * Area Adinda : skenario 1, 5, 6, 7 (bagian "AREA ADINDA")
 * Aturan: masing-masing hanya mengedit areanya sendiri agar merge tidak bentrok.
 */
class ScenarioTest {

    static final String PATH_CSV = "data/putusan-50.csv";
    static final int JUMLAH_DATA = 50;

    private SinglyLinkedList<Putusan> data;

    @BeforeEach
    void muatData() throws IOException {
        CsvImporter importer = new CsvImporter();
        data = importer.importCsv(Path.of(PATH_CSV));
        data.validateInvariant();
    }

    // ================= helper bersama (jangan diubah setelah commit pertama) =================

    /** Node pada posisi tertentu (0-based), lewat traversal dari head. */
    static Node<Putusan> nodeDi(SinglyLinkedList<Putusan> list, int posisi) {
        Node<Putusan> cur = list.getHead();
        for (int i = 0; i < posisi; i++) {
            cur = cur.next;
        }
        return cur;
    }

    /** Salinan isi list sebagai array (urutan head ke tail). */
    static Putusan[] snapshot(SinglyLinkedList<Putusan> list) {
        Putusan[] hasil = new Putusan[list.size()];
        Node<Putusan> cur = list.getHead();
        int i = 0;
        while (cur != null) {
            hasil[i++] = cur.data;
            cur = cur.next;
        }
        return hasil;
    }

    static String id(Putusan p) {
        return p == null ? "null" : p.getCaseId();
    }

    /** Keluaran terminal yang disimpan sebagai bukti di docs/demo/. */
    static void cetak(String teks) {
        System.out.println("[SKENARIO] " + teks);
    }

    // =========================================================================================
    // AREA ADJIE (skenario 2, 3, 4)
    @Test
    void skenario2_cari_ada_tidakAda_dan_posisiBatas() {
        int n = data.size();
        assertEquals(JUMLAH_DATA, n);

        // (a) satu yang ada, di tengah
        Putusan tengah = data.get(n / 2);
        PutusanSearch.Hasil h = PutusanSearch.cariByCaseId(data, tengah.getCaseId());
        assertTrue(h.isDitemukan());
        assertEquals(n / 2, h.getPosisi());
        assertEquals(n / 2 + 1, h.getLangkah());
        assertSame(tengah, h.getPutusan());

        // (b) satu yang tidak ada: seluruh node dibandingkan
        PutusanSearch.Hasil kosong = PutusanSearch.cariByCaseId(data, "TIDAK-ADA-999");
        assertFalse(kosong.isDitemukan());
        assertEquals(-1, kosong.getPosisi());
        assertEquals(n, kosong.getLangkah());
        assertNull(kosong.getPutusan());

        // (c) dua posisi batas: pertama (kasus terbaik) dan terakhir (kasus terburuk jika ada)
        Putusan pertama = data.get(0);
        Putusan terakhir = data.get(n - 1);
        PutusanSearch.Hasil hp = PutusanSearch.cariByCaseId(data, pertama.getCaseId());
        PutusanSearch.Hasil ht = PutusanSearch.cariByCaseId(data, terakhir.getCaseId());
        assertEquals(0, hp.getPosisi());
        assertEquals(1, hp.getLangkah());
        assertEquals(n - 1, ht.getPosisi());
        assertEquals(n, ht.getLangkah());

        // (d) pencarian nomorPerkara
        if (tengah.getNomorPerkara() != null) {
            PutusanSearch.Hasil hn =
                    PutusanSearch.cariByNomorPerkara(data, tengah.getNomorPerkara());
            assertTrue(hn.isDitemukan());
            assertEquals(tengah.getNomorPerkara(), hn.getPutusan().getNomorPerkara());
            assertTrue(hn.getPosisi() <= n / 2);
            assertEquals(hn.getPosisi() + 1, hn.getLangkah());
        }

        // (e) tabel pencacah langkah untuk analisis kompleksitas
        int[] posisiUji = {0, n / 4, n / 2, (3 * n) / 4, n - 1};
        cetak("2. TABEL LANGKAH (n=" + n + ")");
        for (int pos : posisiUji) {
            PutusanSearch.Hasil r = PutusanSearch.cariByCaseId(data, data.get(pos).getCaseId());
            assertEquals(pos + 1, r.getLangkah());
            cetak("2.   posisi " + pos + " -> langkah " + r.getLangkah());
        }
        cetak("2.   tidak ada -> langkah " + kosong.getLangkah() + " (= n)");

        data.validateInvariant();
        assertEquals(n, data.size());
    }
    // [ADJIE-3]
    // [ADJIE-4]
    // =========================================================================================


    // ---------- pemisah: baris-baris ini sengaja tidak diubah siapa pun ----------


    // =========================================================================================
    // AREA ADINDA (skenario 1, 5, 6, 7)

    @Test
    @Disabled("TODO Adinda")
    void skenario1_muat50Record_sizeSama50() {
    }

    @Test
    @Disabled("TODO Adinda")
    void skenario5_filterGabungan_cocokDenganPemeriksaanManual() {
    }

    @Test
    @Disabled("TODO Adinda")
    void skenario6_pencarianStack_tidakMengubahUrutanAkhir() {
    }

    @Test
    @Disabled("TODO Adinda")
    void skenario7_recordTidakValid_pesanKesalahanDanStrukturKonsisten(@TempDir Path tmp)
            throws IOException {
    }
    // =========================================================================================
}