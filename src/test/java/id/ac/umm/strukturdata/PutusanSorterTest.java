package id.ac.umm.strukturdata;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PutusanSorterTest {

    @Test
    void listKosongMenghasilkanListKosongBaru() {
        SinglyLinkedList<Putusan> asli = new SinglyLinkedList<>();
        SinglyLinkedList<Putusan> hasil = PutusanSorter.urutkanByVonis(asli, true);
        assertTrue(hasil.isEmpty());
        assertNotSame(asli, hasil);
    }

    @Test
    void satuElemen() {
        SinglyLinkedList<Putusan> hasil = PutusanSorter.urutkanByVonis(listDari(7), true);
        assertArrayEquals(new int[]{7}, vonisDari(hasil));
    }

    @Test
    void nilaiKembarStabilPadaUrutanNaik() {
        // C1=12, C2=6, C3=12, C4=6
        SinglyLinkedList<Putusan> hasil = PutusanSorter.urutkanByVonis(listDari(12, 6, 12, 6), true);
        assertArrayEquals(new int[]{6, 6, 12, 12}, vonisDari(hasil));
        assertArrayEquals(new String[]{"C2", "C4", "C1", "C3"}, caseIdDari(hasil));
    }

    @Test
    void sudahTerurutTetapTerurut() {
        SinglyLinkedList<Putusan> hasil = PutusanSorter.urutkanByVonis(listDari(1, 2, 3, 4, 5), true);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, vonisDari(hasil));
    }

    @Test
    void urutanTerbalikMenjadiNaik() {
        SinglyLinkedList<Putusan> hasil = PutusanSorter.urutkanByVonis(listDari(5, 4, 3, 2, 1), true);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, vonisDari(hasil));
    }

    @Test
    void urutanTurunDariAcak() {
        SinglyLinkedList<Putusan> hasil = PutusanSorter.urutkanByVonis(listDari(3, 9, 1, 7, 5), false);
        assertArrayEquals(new int[]{9, 7, 5, 3, 1}, vonisDari(hasil));
    }

    @Test
    void nilaiKembarStabilPadaUrutanTurun() {
        // C1=12, C2=6, C3=12, C4=6
        SinglyLinkedList<Putusan> hasil = PutusanSorter.urutkanByVonis(listDari(12, 6, 12, 6), false);
        assertArrayEquals(new int[]{12, 12, 6, 6}, vonisDari(hasil));
        assertArrayEquals(new String[]{"C1", "C3", "C2", "C4"}, caseIdDari(hasil));
    }

    @Test
    void urutanTurunPadaListKosongDanSatuElemen() {
        assertTrue(PutusanSorter.urutkanByVonis(new SinglyLinkedList<>(), false).isEmpty());
        assertArrayEquals(new int[]{4}, vonisDari(PutusanSorter.urutkanByVonis(listDari(4), false)));
    }

    /** List berisi putusan dengan vonis sesuai urutan argumen; caseId = "C1", "C2", ... */
    private SinglyLinkedList<Putusan> listDari(int... vonis) {
        SinglyLinkedList<Putusan> list = new SinglyLinkedList<>();
        for (int i = 0; i < vonis.length; i++) {
            list.addLast(buat("C" + (i + 1), vonis[i]));
        }
        return list;
    }

    private int[] vonisDari(SinglyLinkedList<Putusan> list) {
        int[] hasil = new int[list.size()];
        for (int i = 0; i < hasil.length; i++) {
            hasil[i] = list.get(i).getVonisBulan();
        }
        return hasil;
    }

    private String[] caseIdDari(SinglyLinkedList<Putusan> list) {
        String[] hasil = new String[list.size()];
        for (int i = 0; i < hasil.length; i++) {
            hasil[i] = list.get(i).getCaseId();
        }
        return hasil;
    }

    /** Putusan minimal: hanya caseId & vonisBulan bermakna; sisanya null (teks/tanggal) atau 0 (angka). */
    private Putusan buat(String caseId, int vonisBulan) {
        return new Putusan(
                caseId,
                null,        // nomorPerkara
                null,        // pengadilan
                null,        // tanggalPutusan
                null,        // terdakwaKode
                null,        // jenisNarkotika
                0,           // beratBarangBuktiGram
                null,        // pasalDakwaan
                null,        // pasalTerbukti
                0,           // tuntutanBulan
                vonisBulan,  // vonisBulan
                0L,          // dendaRupiah
                null,        // barangBukti
                null,        // keadaanMemberatkan
                null,        // keadaanMeringankan
                null         // sourcePages
        );
    }
}