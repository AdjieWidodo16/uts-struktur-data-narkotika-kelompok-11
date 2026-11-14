package id.ac.umm.strukturdata;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;


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

                try {
                    hasil.addLast(parseRecord(line));
                } catch (IllegalArgumentException e) {
                    String pesan = "Baris " + nomorBaris + ": " + e.getMessage();
                    errors.addLast(pesan);
                    System.err.println("Impor ditolak - " + pesan);
                }
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

    private Putusan parseRecord(String line) {
        if (line.trim().isEmpty()) {
            throw new IllegalArgumentException("baris kosong");
        }
        String[] f = splitCsv(line);
        if (f.length != JUMLAH_KOLOM) {
            throw new IllegalArgumentException(
                    "jumlah kolom " + f.length + ", seharusnya " + JUMLAH_KOLOM);
        }
        String caseId = teks(f[0]);
        if (caseId == null) {
            throw new IllegalArgumentException("caseId wajib diisi");
        }
        return new Putusan(
                caseId, teks(f[1]), teks(f[2]),
                parseTanggal(f[3], "tanggalPutusan"),
                teks(f[4]), teks(f[5]),
                parseDouble(f[6], "beratBarangBuktiGram"),
                teks(f[7]), teks(f[8]),
                parseInt(f[9], "tuntutanBulan"),
                parseInt(f[10], "vonisBulan"),
                parseLong(f[11], "dendaRupiah"),
                teks(f[12]), teks(f[13]), teks(f[14]), teks(f[15]));
    }

    // ADR-003: tidak ditemukan = angka 0 / teks null. Sel kosong dan kata "null" sama-sama dianggap kosong.
    private static boolean kosong(String t) {
        return t.isEmpty() || t.equalsIgnoreCase("null");
    }

    private static String teks(String s) {
        String t = s.trim();
        return kosong(t) ? null : t;
    }

    private static LocalDate parseTanggal(String s, String kolom) {
        String t = s.trim();
        if (kosong(t)) {
            return null;
        }
        try {
            return LocalDate.parse(t); // ISO yyyy-MM-dd
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(
                    kolom + " bukan tanggal yyyy-MM-dd: \"" + s + "\"");
        }
    }

    private static int parseInt(String s, String kolom) {
        String t = s.trim();
        if (kosong(t)) {
            return 0;
        }
        int v;
        try {
            v = Integer.parseInt(t);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(kolom + " bukan angka: \"" + s + "\"");
        }
        if (v < 0) {
            throw new IllegalArgumentException(kolom + " tidak boleh negatif: " + v);
        }
        return v;
    }

    private static long parseLong(String s, String kolom) {
        String t = s.trim();
        if (kosong(t)) {
            return 0L;
        }
        long v;
        try {
            v = Long.parseLong(t);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(kolom + " bukan angka: \"" + s + "\"");
        }
        if (v < 0) {
            throw new IllegalArgumentException(kolom + " tidak boleh negatif: " + v);
        }
        return v;
    }

    private static double parseDouble(String s, String kolom) {
        String t = s.trim();
        if (kosong(t)) {
            return 0.0;
        }
        double v;
        try {
            v = Double.parseDouble(t);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(kolom + " bukan angka: \"" + s + "\"");
        }
        if (Double.isNaN(v) || Double.isInfinite(v) || v < 0) {
            throw new IllegalArgumentException(
                    kolom + " harus angka tidak negatif: \"" + s + "\"");
        }
        return v;
    }

}