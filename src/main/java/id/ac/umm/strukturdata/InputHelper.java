package id.ac.umm.strukturdata;

import java.util.Scanner;

public class InputHelper {

    /** Dilempar saat input habis (Ctrl+D / pipe selesai) agar program keluar dengan rapi. */
    public static class InputBerakhirException extends RuntimeException {
        public InputBerakhirException() {
            super("Input berakhir");
        }
    }

    private final Scanner scanner;

    public InputHelper(Scanner scanner) {
        if (scanner == null) {
            throw new IllegalArgumentException("Scanner tidak boleh null");
        }
        this.scanner = scanner;
    }

    public String bacaBaris(String prompt) {
        System.out.print(prompt);
        if (!scanner.hasNextLine()) {
            throw new InputBerakhirException();
        }
        return scanner.nextLine().trim();
    }

    public int bacaInt(String prompt) {
        while (true) {
            String s = bacaBaris(prompt);
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                System.out.println("Input harus berupa angka bulat. Coba lagi.");
            }
        }
    }

    public int bacaIntDalam(String prompt, int min, int max) {
        while (true) {
            int v = bacaInt(prompt);
            if (v >= min && v <= max) {
                return v;
            }
            System.out.println("Pilihan harus antara " + min + " dan " + max + ".");
        }
    }

    public String bacaTeksWajib(String prompt) {
        while (true) {
            String s = bacaBaris(prompt);
            if (!s.isEmpty()) {
                return s;
            }
            System.out.println("Input tidak boleh kosong.");
        }
    }

    /** Kosong menghasilkan null (data tidak ditemukan, ADR-003). */
    public String bacaTeksOpsional(String prompt) {
        String s = bacaBaris(prompt);
        return s.isEmpty() ? null : s;
    }

    public boolean bacaYa(String prompt) {
        while (true) {
            String s = bacaBaris(prompt).toLowerCase();
            if (s.equals("y") || s.equals("ya")) return true;
            if (s.equals("n") || s.equals("t") || s.equals("tidak")) return false;
            System.out.println("Jawab y atau n.");
        }
    }
    // [COMMIT 3] tempel method baru di sini
}