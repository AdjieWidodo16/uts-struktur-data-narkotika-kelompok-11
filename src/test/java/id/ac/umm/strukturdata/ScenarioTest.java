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
    // [ADJIE-2]
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