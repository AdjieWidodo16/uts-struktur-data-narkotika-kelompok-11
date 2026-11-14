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
    @Test
    void skenario3_hapus_pertama_tengah_terakhir_dan_perubahanPointer() {
        int n = data.size();

        // ---- hapus pertama: head maju ke node kedua ----
        Node<Putusan> head0 = data.getHead();
        Node<Putusan> head1 = head0.next;
        Putusan dihapus1 = data.remove(0);
        assertSame(head0.data, dihapus1);
        assertSame(head1, data.getHead(), "head harus maju ke node kedua");
        assertEquals(n - 1, data.size());
        data.validateInvariant();
        cetak("3a. hapus pertama " + id(dihapus1) + " | head: " + id(head0.data)
                + " -> " + id(data.getHead().data) + " | size " + data.size());

        // ---- hapus tengah: node sebelumnya melompati target ----
        int k = data.size() / 2;
        Node<Putusan> prev = nodeDi(data, k - 1);
        Node<Putusan> target = prev.next;
        Node<Putusan> sesudah = target.next;
        Putusan dihapus2 = data.remove(k);
        assertSame(target.data, dihapus2);
        assertSame(sesudah, prev.next, "prev.next harus melompati node yang dihapus");
        assertEquals(n - 2, data.size());
        data.validateInvariant();
        cetak("3b. hapus tengah posisi " + k + " " + id(dihapus2) + " | " + id(prev.data)
                + ".next: " + id(target.data) + " -> " + id(sesudah.data)
                + " | size " + data.size());

        // ---- hapus terakhir: tail mundur ke node sebelumnya ----
        int akhir = data.size() - 1;
        Node<Putusan> prevAkhir = nodeDi(data, akhir - 1);
        Putusan dihapus3 = data.remove(akhir);
        assertNull(prevAkhir.next, "node terakhir baru harus menunjuk null");
        assertSame(prevAkhir, nodeDi(data, data.size() - 1));
        assertEquals(n - 3, data.size());
        data.validateInvariant();            // juga memeriksa tail == node terakhir

        // bukti tail ikut mundur: addLast harus tersambung ke prevAkhir
        data.addLast(dihapus3);
        assertSame(dihapus3, prevAkhir.next.data);
        data.validateInvariant();
        cetak("3c. hapus terakhir " + id(dihapus3) + " | tail mundur ke " + id(prevAkhir.data)
                + " | addLast tersambung ke " + id(prevAkhir.data));
    }
    @Test
    void skenario4_tambahKembaliTigaRecord_invariantDanUrutan() {
        int n = data.size();
        Putusan a = data.remove(0);
        Putusan b = data.remove(data.size() / 2);
        Putusan c = data.remove(data.size() - 1);
        int m = data.size();
        assertEquals(n - 3, m);
        Putusan[] urutanLama = snapshot(data);

        data.addLast(c);                         // c ke akhir
        data.validateInvariant();
        assertSame(c, data.get(m));

        data.addFirst(a);                        // a ke awal, sisanya bergeser +1
        data.validateInvariant();
        assertSame(a, data.get(0));
        assertSame(urutanLama[0], data.get(1));

        int k = data.size() / 2;                 // b disisipkan di tengah
        data.insert(k, b);
        data.validateInvariant();

        assertEquals(n, data.size());
        assertSame(a, data.get(0));
        assertSame(b, data.get(k));
        assertSame(c, data.get(n - 1));

        // record lama tetap berurutan, hanya bergeser
        int idxLama = 0;
        for (int i = 1; i < n - 1; i++) {
            if (i == k) {
                continue;
            }
            assertSame(urutanLama[idxLama++], data.get(i), "Urutan lama rusak di posisi " + i);
        }
        assertEquals(m, idxLama);

        cetak("4. tambah kembali: addFirst " + id(a) + " (pos 0), insert " + id(b)
                + " (pos " + k + "), addLast " + id(c) + " (pos " + (n - 1) + ") | size " + data.size()
                + " | invariant OK");
    }
    // =========================================================================================


    // ---------- pemisah: baris-baris ini sengaja tidak diubah siapa pun ----------


    // =========================================================================================
    // AREA ADINDA (skenario 1, 5, 6, 7)

    @Test
    void skenario1_muat50Record_sizeSama50() throws IOException {
        CsvImporter importer = new CsvImporter();
        SinglyLinkedList<Putusan> hasil = importer.importCsv(Path.of(PATH_CSV));

        StringBuilder alasan = new StringBuilder();
        Node<String> e = importer.getErrors().getHead();
        while (e != null) {
            alasan.append("\n  - ").append(e.data);
            e = e.next;
        }

        assertEquals(JUMLAH_DATA, hasil.size(), "size harus 50");
        assertEquals(JUMLAH_DATA, importer.getJumlahDibaca());
        assertEquals(0, importer.getJumlahDitolak(), "Ada record ditolak:" + alasan);
        hasil.validateInvariant();

        // caseId harus unik
        Putusan[] semua = snapshot(hasil);
        for (int i = 0; i < semua.length; i++) {
            for (int j = i + 1; j < semua.length; j++) {
                assertNotEquals(semua[i].getCaseId(), semua[j].getCaseId(),
                        "caseId ganda pada posisi " + i + " dan " + j);
            }
        }

        cetak("1. dibaca=" + importer.getJumlahDibaca() + ", valid=" + hasil.size()
                + ", ditolak=" + importer.getJumlahDitolak()
                + ", pertama=" + id(semua[0]) + ", terakhir=" + id(semua[semua.length - 1]));
    }

    // Isi setelah pemeriksaan manual ke putusan asli: caseId hasil filter, urut sesuai list.
    // Biarkan kosong sebelum pemeriksaan manual selesai (bagian itu akan dilewati).
    static final String[] CASE_ID_HASIL_MANUAL = {};

    /** Pemeriksaan independen tanpa PutusanFilter (hanya String.toLowerCase().contains()). */
    static boolean cocokManual(Putusan p, String jenis, double bMin, double bMax,
                               String pasal, int vMin, int vMax) {
        if (p.getJenisNarkotika() == null
                || !p.getJenisNarkotika().toLowerCase().contains(jenis.toLowerCase())) return false;
        double b = p.getBeratBarangBuktiGram();
        if (b <= 0 || b < bMin || b > bMax) return false;
        if (p.getPasalTerbukti() == null
                || !p.getPasalTerbukti().toLowerCase().contains(pasal.toLowerCase())) return false;
        int v = p.getVonisBulan();
        return v > 0 && v >= vMin && v <= vMax;
    }

    @Test
    void skenario5_filterGabungan_cocokDenganPemeriksaanManual() {
        // Cari record acuan yang datanya lengkap, lalu bangun kriteria dari nilainya
        Putusan acuan = null;
        Node<Putusan> cur = data.getHead();
        while (cur != null && acuan == null) {
            Putusan p = cur.data;
            if (p.getJenisNarkotika() != null && p.getPasalTerbukti() != null
                    && p.getBeratBarangBuktiGram() > 0 && p.getVonisBulan() > 0) {
                acuan = p;
            }
            cur = cur.next;
        }
        assertNotNull(acuan, "Tidak ada record dengan jenis, pasal, berat, dan vonis lengkap");

        String jenis = acuan.getJenisNarkotika();
        String pasal = acuan.getPasalTerbukti();
        double bMin = acuan.getBeratBarangBuktiGram() * 0.5;
        double bMax = acuan.getBeratBarangBuktiGram() * 2.0;
        int vMin = Math.max(1, acuan.getVonisBulan() - 12);
        int vMax = acuan.getVonisBulan() + 12;

        PutusanFilter f = new PutusanFilter()
                .jenisNarkotika(jenis)
                .beratBetween(bMin, bMax)
                .pasalTerbukti(pasal)
                .vonisBetween(vMin, vMax);
        assertEquals(4, f.jumlahKriteriAktif());

        SinglyLinkedList<Putusan> hasil = f.terapkan(data);
        hasil.validateInvariant();

        // Pembanding independen: urutan dan isi harus sama persis
        Putusan[] semua = snapshot(data);
        int diharapkan = 0;
        Putusan[] hasilArr = snapshot(hasil);
        for (Putusan p : semua) {
            if (cocokManual(p, jenis, bMin, bMax, pasal, vMin, vMax)) {
                assertTrue(diharapkan < hasilArr.length, "Filter kehilangan " + id(p));
                assertSame(p, hasilArr[diharapkan], "Urutan/isi beda di indeks " + diharapkan);
                diharapkan++;
            }
        }
        assertEquals(diharapkan, hasilArr.length, "Filter mengembalikan record berlebih");
        assertTrue(hasilArr.length >= 1, "Record acuan harus lolos");

        // Daftar ini yang dicocokkan ke dokumen putusan asli (provenance) lewat sourcePages
        cetak("5. kriteria: jenis=" + jenis + ", berat=" + bMin + ".." + bMax
                + ", pasal=" + pasal + ", vonis=" + vMin + ".." + vMax);
        for (Putusan p : hasilArr) {
            cetak("5.   " + id(p) + " | " + p.getNomorPerkara() + " | hal " + p.getSourcePages());
        }
        cetak("5. hasil=" + hasilArr.length + " dari " + data.size());

        // Pemeriksaan manual (aktif setelah CASE_ID_HASIL_MANUAL diisi)
        if (CASE_ID_HASIL_MANUAL.length > 0) {
            assertEquals(CASE_ID_HASIL_MANUAL.length, hasilArr.length);
            for (int i = 0; i < hasilArr.length; i++) {
                assertEquals(CASE_ID_HASIL_MANUAL[i], hasilArr[i].getCaseId());
            }
        }

        // List asli tidak berubah, dan filter tanpa hasil menghasilkan list kosong yang valid
        assertEquals(JUMLAH_DATA, data.size());
        SinglyLinkedList<Putusan> kosong =
                new PutusanFilter().jenisNarkotika("zzz-tidak-ada").terapkan(data);
        assertEquals(0, kosong.size());
        kosong.validateInvariant();
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