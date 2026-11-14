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

    // [COMMIT 2] tempel method baru di sini
    // [COMMIT 3] tempel method baru di sini
}