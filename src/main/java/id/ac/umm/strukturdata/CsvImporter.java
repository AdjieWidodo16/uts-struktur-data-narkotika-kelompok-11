package id.ac.umm.strukturdata;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Mengimpor data/putusan-50.csv ke SinglyLinkedList<Putusan>.
 * Record tidak valid ditolak dan dicatat di errors; impor tetap berlanjut.
 */
public class CsvImporter {

    static final int JUMLAH_KOLOM = 16;

    private final SinglyLinkedList<String> errors = new SinglyLinkedList<>();
    private int jumlahDibaca;

    public SinglyLinkedList<String> getErrors() { return errors; }
    public int getJumlahDitolak() { return errors.size(); }
    public int getJumlahDibaca() { return jumlahDibaca; }

    public SinglyLinkedList<Putusan> importCsv(Path file) throws IOException {
        SinglyLinkedList<Putusan> hasil = new SinglyLinkedList<>();
        errors.clear();
        jumlahDibaca = 0;

        try (BufferedReader br = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            br.readLine(); // lewati header
            int nomorBaris = 1;
            String line;
            while ((line = br.readLine()) != null) {
                nomorBaris++;
                jumlahDibaca++;
            }
        }
        return hasil;
    }

    /** Memecah satu baris CSV; koma di dalam tanda kutip tidak memisahkan kolom. */
    static String[] splitCsv(String line) {
        String[] out = new String[hitungKolom(line)];
        StringBuilder sb = new StringBuilder();
        int idx = 0;
        boolean inQ = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                if (inQ && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    sb.append('"');   // "" di dalam kutip = satu tanda kutip
                    i++;
                } else {
                    inQ = !inQ;
                }
            } else if (c == ',' && !inQ) {
                out[idx++] = sb.toString();
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }
        out[idx] = sb.toString();
        return out;
    }

    // Hitung kolom dulu supaya array bisa dialokasikan tanpa ArrayList.
    private static int hitungKolom(String line) {
        int n = 1;
        boolean inQ = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                if (inQ && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    i++;
                } else {
                    inQ = !inQ;
                }
            } else if (c == ',' && !inQ) {
                n++;
            }
        }
        if (inQ) {
            throw new IllegalArgumentException("tanda kutip tidak tertutup");
        }
        return n;
    }


}