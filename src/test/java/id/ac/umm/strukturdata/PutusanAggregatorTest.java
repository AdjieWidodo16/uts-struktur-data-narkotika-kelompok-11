package id.ac.umm.strukturdata;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PutusanAggregatorTest {

    @Test
    void jenisCountMenyimpanJenisDanBertambah() {
        PutusanAggregator.JenisCount c = new PutusanAggregator.JenisCount("Sabu", 1);
        assertEquals("Sabu", c.getJenis());
        assertEquals(1, c.getJumlah());
        c.tambah();
        assertEquals(2, c.getJumlah());
    }

    // Data manual: vonis 12+24+6+18 = 60 -> rata-rata 60/4 = 15.0
    //              denda 1.000.000+2.000.000+0+500.000 = 3.500.000
    private SinglyLinkedList<Putusan> data4() {
        SinglyLinkedList<Putusan> list = new SinglyLinkedList<>();
        list.addLast(buat("Sabu", 12, 1_000_000L));
        list.addLast(buat("Ganja", 24, 2_000_000L));
        list.addLast(buat("Sabu", 6, 0L));
        list.addLast(buat("Sabu", 18, 500_000L));
        return list;
    }

    @Test
    void listKosongTidakMenyebabkanPembagianDenganNol() {
        PutusanAggregator.Ringkasan r = PutusanAggregator.hitung(new SinglyLinkedList<>());
        assertEquals(0, r.getJumlahPerkara());
        assertEquals(0.0, r.getRataRataVonisBulan());
        assertEquals(0L, r.getTotalDendaRupiah());
        assertEquals(0, r.getBanyakJenis());
    }

    @Test
    void jumlahPerkaraDanRataRataVonis() {
        PutusanAggregator.Ringkasan r = PutusanAggregator.hitung(data4());
        assertEquals(4, r.getJumlahPerkara());
        assertEquals(15.0, r.getRataRataVonisBulan(), 1e-9);
    }

    @Test
    void rataRataBerupaDesimal() {
        // vonis 10 + 10 + 11 = 31 -> 31/3
        SinglyLinkedList<Putusan> list = new SinglyLinkedList<>();
        list.addLast(buat("Sabu", 10, 0L));
        list.addLast(buat("Sabu", 10, 0L));
        list.addLast(buat("Sabu", 11, 0L));
        assertEquals(31.0 / 3, PutusanAggregator.hitung(list).getRataRataVonisBulan(), 1e-9);
    }

    @Test
    void totalDendaDijumlahkan() {
        assertEquals(3_500_000L, PutusanAggregator.hitung(data4()).getTotalDendaRupiah());
    }

    @Test
    void totalDendaMelebihiBatasInt() {
        SinglyLinkedList<Putusan> list = new SinglyLinkedList<>();
        list.addLast(buat("Sabu", 1, 3_000_000_000L));
        list.addLast(buat("Sabu", 1, 3_000_000_000L));
        assertEquals(6_000_000_000L, PutusanAggregator.hitung(list).getTotalDendaRupiah());
    }

    @Test
    void satuElemen() {
        SinglyLinkedList<Putusan> list = new SinglyLinkedList<>();
        list.addLast(buat("Ganja", 9, 250_000L));
        PutusanAggregator.Ringkasan r = PutusanAggregator.hitung(list);
        assertEquals(1, r.getJumlahPerkara());
        assertEquals(9.0, r.getRataRataVonisBulan(), 1e-9);
        assertEquals(250_000L, r.getTotalDendaRupiah());
    }

    @Test
    void distribusiJenisDihitungManual() {
        // Sabu = 3 perkara, Ganja = 1 perkara
        PutusanAggregator.Ringkasan r = PutusanAggregator.hitung(data4());
        assertEquals(2, r.getBanyakJenis());
        assertEquals(3, r.jumlahJenis("Sabu"));
        assertEquals(1, r.jumlahJenis("Ganja"));
    }

    @Test
    void jenisTidakAdaBernilaiNol() {
        assertEquals(0, PutusanAggregator.hitung(data4()).jumlahJenis("Heroin"));
    }

    @Test
    void jenisNullDikelompokkanTersendiri() {
        SinglyLinkedList<Putusan> list = new SinglyLinkedList<>();
        list.addLast(buat(null, 1, 0L));
        list.addLast(buat("Sabu", 1, 0L));
        list.addLast(buat(null, 1, 0L));
        PutusanAggregator.Ringkasan r = PutusanAggregator.hitung(list);
        assertEquals(2, r.getBanyakJenis());
        assertEquals(2, r.jumlahJenis(null));
        assertEquals(1, r.jumlahJenis("Sabu"));
    }

    @Test
    void jumlahSemuaDistribusiSamaDenganJumlahPerkara() {
        PutusanAggregator.Ringkasan r = PutusanAggregator.hitung(data4());
        int total = 0;
        Node<PutusanAggregator.JenisCount> cur = r.getDistribusi().getHead();
        while (cur != null) {
            total += cur.data.getJumlah();
            cur = cur.next;
        }
        assertEquals(r.getJumlahPerkara(), total);
    }

    /** Putusan minimal: hanya jenisNarkotika, vonisBulan, dendaRupiah bermakna; sisanya null/0. */
    private Putusan buat(String jenisNarkotika, int vonisBulan, long dendaRupiah) {
        return new Putusan(
                "C",              // caseId
                null,             // nomorPerkara
                null,             // pengadilan
                null,             // tanggalPutusan
                null,             // terdakwaKode
                jenisNarkotika,   // jenisNarkotika
                0,                // beratBarangBuktiGram
                null,             // pasalDakwaan
                null,             // pasalTerbukti
                0,                // tuntutanBulan
                vonisBulan,       // vonisBulan
                dendaRupiah,      // dendaRupiah
                null,             // barangBukti
                null,             // keadaanMemberatkan
                null,             // keadaanMeringankan
                null              // sourcePages
        );
    }

}