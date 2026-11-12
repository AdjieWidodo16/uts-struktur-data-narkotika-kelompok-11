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
    private T[] holding;       // area penampung sementara saat pencarian
    private int holdingSize;
    private int lastSearchSteps;

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

    public void push(T value) {
        if (value == null) {
            throw new IllegalArgumentException("Elemen audit tidak boleh null");
        }
        if (size == data.length) {
            resize();
        }
        data[size] = value;
        size++;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        T[] baru = (T[]) new Object[data.length * 2];
        for (int i = 0; i < size; i++) {
            baru[i] = data[i];
        }
        data = baru;
    }

    public T peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack kosong: tidak ada elemen untuk dilihat (peek)");
        }
        return data[size - 1];
    }

    public T pop() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack kosong: tidak ada elemen untuk diambil (pop)");
        }
        T value = data[size - 1];
        data[size - 1] = null; // lepas referensi agar slot di atas puncak selalu null
        size--;
        return value;
    }

    public void clear() {
        for (int i = 0; i < size; i++) {
            data[i] = null;
        }
        size = 0;
        holding = null;
        holdingSize = 0;
    }

    public boolean contains(T value) {
        for (int i = size - 1; i >= 0; i--) {
            if (sama(data[i], value)) {
                return true;
            }
        }
        return false;
    }

    private static boolean sama(Object a, Object b) {
        return a == null ? b == null : a.equals(b);
    }

    /**
     * Mencari target dari puncak ke dasar tanpa mengubah urutan akhir stack.
     * @return kedalaman dari puncak (0 = puncak), atau -1 jika tidak ditemukan.
     *         Jumlah langkah (elemen yang diperiksa) ada di getLastSearchSteps().
     */
    @SuppressWarnings("unchecked")
    public int searchPreserve(T target) {
        lastSearchSteps = 0;
        holding = (T[]) new Object[size];
        holdingSize = 0;

        int hasil = -1;
        int kedalaman = 0;
        while (!isEmpty()) {
            T item = pop();
            holding[holdingSize] = item;
            holdingSize++;
            lastSearchSteps++;
            if (sama(item, target)) {
                hasil = kedalaman;
                break;
            }
            kedalaman++;
        }
        restore();
        return hasil;
    }

    /** Mengembalikan elemen yang tertahan ke stack sehingga urutan seperti semula. */
    public void restore() {
        while (holdingSize > 0) {
            holdingSize--;
            push(holding[holdingSize]);
            holding[holdingSize] = null;
        }
        holding = null;
    }

    public int getLastSearchSteps() {
        return lastSearchSteps;
    }




}