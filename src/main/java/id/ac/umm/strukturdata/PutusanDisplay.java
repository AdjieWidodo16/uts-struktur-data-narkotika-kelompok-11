package id.ac.umm.strukturdata;

public class PutusanDisplay {

    /**
     * Menyusun ringkasan semua putusan sesuai urutan list (dari head ke tail).
     * List tidak diubah. Nilai teks null ditampilkan sebagai "-".
     */
    public static String ringkasan(SinglyLinkedList<Putusan> list) {
        if (list == null) {
            throw new IllegalArgumentException("List putusan tidak boleh null");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Total: ").append(list.size()).append(" putusan\n");
        if (list.isEmpty()) {
            sb.append("(Tidak ada putusan)\n");
            return sb.toString();
        }
        int no = 1;
        Node<Putusan> cur = list.getHead();
        while (cur != null) {
            Putusan p = cur.data;
            sb.append(no).append(". ")
                    .append(teks(p.getCaseId())).append(" | ")
                    .append(teks(p.getNomorPerkara())).append(" | ")
                    .append(teks(p.getPengadilan())).append(" | ")
                    .append(teks(p.getJenisNarkotika())).append(" | ")
                    .append(p.getBeratBarangBuktiGram()).append(" g | vonis ")
                    .append(p.getVonisBulan()).append(" bln\n");
            cur = cur.next;
            no++;
        }
        return sb.toString();
    }

    public static void tampilkan(SinglyLinkedList<Putusan> list) {
        System.out.print(ringkasan(list));
    }

    private static String teks(String nilai) {
        return nilai == null ? "-" : nilai;
    }

    private PutusanDisplay() { }
}