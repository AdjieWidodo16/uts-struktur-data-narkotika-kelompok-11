package id.ac.umm.strukturdata;

public class SinglyLinkedList<T> {
    private Node<T> head;
    private Node<T> tail;
    private int size;

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void addFirst(T value) {
        Node<T> node = new Node<>(value);
        node.next = head;
        head = node;
        if (tail == null) {
            tail = node;
        }
        size++;
    }

    public void addLast(T value) {
        Node<T> node = new Node<>(value);
        if (tail == null) {
            head = node;
            tail = node;
        } else {
            tail.next = node;
            tail = node;
        }
        size++;
    }

    public T get(int position) {
        checkIndex(position);
        Node<T> current = head;
        for (int i = 0; i < position; i++) {
            current = current.next;
        }
        return current.data;
    }

    private void checkIndex(int position) {
        if (position < 0 || position >= size) {
            throw new IndexOutOfBoundsException(
                    "Posisi " + position + " di luar batas, size = " + size);
        }
    }

    public void insert(int position, T value) {
        if (position < 0 || position > size) {
            throw new IndexOutOfBoundsException(
                    "Posisi " + position + " di luar batas, size = " + size);
        }
        if (position == 0) {
            addFirst(value);
            return;
        }
        if (position == size) {
            addLast(value);
            return;
        }
        Node<T> prev = head;
        for (int i = 0; i < position - 1; i++) {
            prev = prev.next;
        }
        Node<T> node = new Node<>(value);
        node.next = prev.next;
        prev.next = node;
        size++;
    }

    public T remove(int position) {
        checkIndex(position);
        if (position == 0) {
            T removed = head.data;
            head = head.next;
            if (head == null) {
                tail = null; // list jadi kosong
            }
            size--;
            return removed;
        }
        throw new UnsupportedOperationException("belum diimplementasi");
    }


}