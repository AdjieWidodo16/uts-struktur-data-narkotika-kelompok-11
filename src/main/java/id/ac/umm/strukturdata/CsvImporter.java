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
        }
        return hasil;
    }
}