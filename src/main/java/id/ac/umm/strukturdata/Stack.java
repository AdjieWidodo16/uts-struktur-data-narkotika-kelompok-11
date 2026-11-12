package id.ac.umm.strukturdata;

/**
 * Stack generik (LIFO) berbasis array, dibuat dari nol.
 * Dipakai sebagai struktur sekunder untuk riwayat operasi (fitur Audit).
 * Index 0 = dasar stack, index size-1 = puncak (top).
 */
public class Stack<T> {

    private static final int DEFAULT_CAPACITY = 10;

    private T[] data;
    private int size;

    public Stack() {
        this(DEFAULT_CAPACITY);
    }

    @SuppressWarnings("unchecked")
    public Stack(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Kapasitas awal harus lebih dari 0");
        }
        this.data = (T[]) new Object[initialCapacity];
        this.size = 0;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int capacity() {
        return data.length;
    }
}