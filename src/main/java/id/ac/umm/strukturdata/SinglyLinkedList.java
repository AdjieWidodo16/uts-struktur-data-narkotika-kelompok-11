package id.ac.umm.strukturdata;

public class SinglyLinkedList<T> implements StrukturData<T> {
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

    @Override
    public void add(T value) {
        addLast(value);
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

        Node<T> prev = head;
        for (int i = 0; i < position - 1; i++) {
            prev = prev.next;
        }
        Node<T> target = prev.next;
        prev.next = target.next;
        if (target == tail) {
            tail = prev;
        }
        size--;
        return target.data;
    }

    private int lastSearchSteps;

    public int getLastSearchSteps() {
        return lastSearchSteps;
    }

    // mengembalikan posisi pertama yang cocok, atau -1 kalau tidak ada
    public int search(T value) {
        lastSearchSteps = 0;
        Node<T> current = head;
        int index = 0;
        while (current != null) {
            lastSearchSteps++;
            boolean same = (value == null)
                    ? current.data == null
                    : value.equals(current.data);
            if (same) {
                return index;
            }
            current = current.next;
            index++;
        }
        return -1;
    }

    public boolean contains(T value) {
        return search(value) != -1;
    }

    public void clear() {
        head = null;
        tail = null;
        size = 0;
        lastSearchSteps = 0;
    }

    public void validateInvariant() {
        if (size < 0) {
            throw new IllegalStateException("size negatif: " + size);
        }
        if (size == 0) {
            if (head != null || tail != null) {
                throw new IllegalStateException("List kosong tapi head/tail tidak null");
            }
            return;
        }
        if (head == null || tail == null) {
            throw new IllegalStateException("List tidak kosong tapi head/tail null");
        }
        if (tail.next != null) {
            throw new IllegalStateException("tail.next harus null");
        }
        int count = 0;
        Node<T> current = head;
        Node<T> last = null;
        while (current != null && count <= size) { // batas count mencegah loop tak hingga jika ada siklus
            count++;
            last = current;
            current = current.next;
        }
        if (count != size) {
            throw new IllegalStateException(
                    "Jumlah node " + count + " tidak sama dengan size " + size);
        }
        if (last != tail) {
            throw new IllegalStateException("Node terakhir bukan tail");
        }
    }
}