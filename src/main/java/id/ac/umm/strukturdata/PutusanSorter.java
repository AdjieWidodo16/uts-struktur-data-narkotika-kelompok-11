package id.ac.umm.strukturdata;

/**
 * Pengurutan putusan berdasarkan vonisBulan dengan merge sort buatan sendiri.
 * Hasilnya list BARU; list asli hanya dibaca, tidak pernah diubah.
 * Merge sort stabil: pada nilai kembar, urutan awal dipertahankan.
 * Kompleksitas O(n log n), lihat ADR-002.
 */
public final class PutusanSorter {

    private PutusanSorter() { }

    /**
     * @param list list asli (tidak diubah)
     * @param naik true = vonis kecil ke besar, false = vonis besar ke kecil
     * @return list baru yang sudah terurut
     */
    public static SinglyLinkedList<Putusan> urutkanByVonis(SinglyLinkedList<Putusan> list, boolean naik) {
        int n = list.size();
        if (n <= 1) {
            return salin(list);
        }

        // bagi dua: kiri berisi n/2 elemen pertama
        int tengah = n / 2;
        SinglyLinkedList<Putusan> kiri = new SinglyLinkedList<>();
        SinglyLinkedList<Putusan> kanan = new SinglyLinkedList<>();
        Node<Putusan> cur = list.getHead();
        for (int i = 0; i < n; i++) {
            if (i < tengah) {
                kiri.addLast(cur.data);
            } else {
                kanan.addLast(cur.data);
            }
            cur = cur.next;
        }

        return gabung(urutkanByVonis(kiri, naik), urutkanByVonis(kanan, naik), naik);
    }

    // menggabungkan dua list terurut menjadi satu list baru terurut
    private static SinglyLinkedList<Putusan> gabung(SinglyLinkedList<Putusan> a,
                                                    SinglyLinkedList<Putusan> b,
                                                    boolean naik) {
        SinglyLinkedList<Putusan> hasil = new SinglyLinkedList<>();
        Node<Putusan> pa = a.getHead();
        Node<Putusan> pb = b.getHead();
        while (pa != null && pb != null) {
            if (kiriDulu(pa.data, pb.data, naik)) {
                hasil.addLast(pa.data);
                pa = pa.next;
            } else {
                hasil.addLast(pb.data);
                pb = pb.next;
            }
        }
        while (pa != null) {
            hasil.addLast(pa.data);
            pa = pa.next;
        }
        while (pb != null) {
            hasil.addLast(pb.data);
            pb = pb.next;
        }
        return hasil;
    }

    // sama-dengan masuk ke kiri supaya stabil
    private static boolean kiriDulu(Putusan kiri, Putusan kanan, boolean naik) {
        return naik
                ? kiri.getVonisBulan() <= kanan.getVonisBulan()
                : kiri.getVonisBulan() >= kanan.getVonisBulan();
    }

    private static SinglyLinkedList<Putusan> salin(SinglyLinkedList<Putusan> list) {
        SinglyLinkedList<Putusan> hasil = new SinglyLinkedList<>();
        Node<Putusan> cur = list.getHead();
        while (cur != null) {
            hasil.addLast(cur.data);
            cur = cur.next;
        }
        return hasil;
    }
}