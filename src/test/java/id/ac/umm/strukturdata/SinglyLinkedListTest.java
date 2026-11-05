package id.ac.umm.strukturdata;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class SinglyLinkedListTest {

    @Test
    void newListIsEmpty() {
        SinglyLinkedList<String> list = new SinglyLinkedList<>();
        assertTrue(list.isEmpty());
        assertEquals(0, list.size());
    }

    @Test
    void addFirstIncreasesSize() {
        SinglyLinkedList<String> list = new SinglyLinkedList<>();
        list.addFirst("A");
        list.addFirst("B");
        assertFalse(list.isEmpty());
        assertEquals(2, list.size());
    }

    @Test
    void addLastIncreasesSize() {
        SinglyLinkedList<String> list = new SinglyLinkedList<>();
        list.addLast("A");
        list.addLast("B");
        list.addLast("C");
        assertEquals(3, list.size());
    }

    @Test
    void mixedAddsCountCorrectly() {
        SinglyLinkedList<Integer> list = new SinglyLinkedList<>();
        list.addLast(1);
        list.addFirst(0);
        list.addLast(2);
        assertEquals(3, list.size());
    }

    @Test
    void addFirstAndAddLastKeepOrder() {
        SinglyLinkedList<String> list = new SinglyLinkedList<>();
        list.addLast("B");
        list.addFirst("A");
        list.addLast("C");
        assertEquals("A", list.get(0));
        assertEquals("B", list.get(1));
        assertEquals("C", list.get(2));
    }

    @Test
    void getOutOfBoundsThrows() {
        SinglyLinkedList<String> list = new SinglyLinkedList<>();
        list.addLast("A");
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(1));
    }

    @Test
    void insertAtHeadMiddleAndTail() {
        SinglyLinkedList<String> list = new SinglyLinkedList<>();
        list.addLast("B");
        list.addLast("D");
        list.insert(0, "A");
        list.insert(2, "C");
        list.insert(4, "E");
        assertEquals(5, list.size());
        assertEquals("A", list.get(0));
        assertEquals("B", list.get(1));
        assertEquals("C", list.get(2));
        assertEquals("D", list.get(3));
        assertEquals("E", list.get(4));
    }

    @Test
    void insertIntoEmptyListAtZero() {
        SinglyLinkedList<String> list = new SinglyLinkedList<>();
        list.insert(0, "A");
        assertEquals(1, list.size());
        assertEquals("A", list.get(0));
    }

    @Test
    void insertOutOfBoundsThrows() {
        SinglyLinkedList<String> list = new SinglyLinkedList<>();
        assertThrows(IndexOutOfBoundsException.class, () -> list.insert(-1, "X"));
        assertThrows(IndexOutOfBoundsException.class, () -> list.insert(1, "X"));
    }

    @Test
    void removeFirstMovesHead() {
        SinglyLinkedList<String> list = new SinglyLinkedList<>();
        list.addLast("A");
        list.addLast("B");
        assertEquals("A", list.remove(0));
        assertEquals(1, list.size());
        assertEquals("B", list.get(0));
    }

    @Test
    void removeOnlyElementEmptiesList() {
        SinglyLinkedList<String> list = new SinglyLinkedList<>();
        list.addLast("A");
        assertEquals("A", list.remove(0));
        assertTrue(list.isEmpty());
        list.addLast("B"); // pastikan tail tidak rusak
        assertEquals("B", list.get(0));
    }

    @Test
    void removeMiddleRelinksNodes() {
        SinglyLinkedList<String> list = new SinglyLinkedList<>();
        list.addLast("A");
        list.addLast("B");
        list.addLast("C");
        assertEquals("B", list.remove(1));
        assertEquals(2, list.size());
        assertEquals("A", list.get(0));
        assertEquals("C", list.get(1));
    }

    @Test
    void removeLastUpdatesTail() {
        SinglyLinkedList<String> list = new SinglyLinkedList<>();
        list.addLast("A");
        list.addLast("B");
        assertEquals("B", list.remove(1));
        list.addLast("C"); // kalau tail salah, C tidak akan tersambung
        assertEquals(2, list.size());
        assertEquals("C", list.get(1));
    }

    @Test
    void removeOutOfBoundsThrows() {
        SinglyLinkedList<String> list = new SinglyLinkedList<>();
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(0));
        list.addLast("A");
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(-1));
    }

    @Test
    void searchFindsFirstElementInOneStep() {
        SinglyLinkedList<String> list = new SinglyLinkedList<>();
        list.addLast("A");
        list.addLast("B");
        list.addLast("C");
        assertEquals(0, list.search("A"));
        assertEquals(1, list.getLastSearchSteps());
    }

    @Test
    void searchFindsLastElementInSizeSteps() {
        SinglyLinkedList<String> list = new SinglyLinkedList<>();
        list.addLast("A");
        list.addLast("B");
        list.addLast("C");
        assertEquals(2, list.search("C"));
        assertEquals(3, list.getLastSearchSteps());
    }

    @Test
    void searchMissingReturnsMinusOneAndScansAll() {
        SinglyLinkedList<String> list = new SinglyLinkedList<>();
        list.addLast("A");
        list.addLast("B");
        assertEquals(-1, list.search("Z"));
        assertEquals(2, list.getLastSearchSteps());
    }

    @Test
    void searchOnEmptyListTakesZeroSteps() {
        SinglyLinkedList<String> list = new SinglyLinkedList<>();
        assertEquals(-1, list.search("A"));
        assertEquals(0, list.getLastSearchSteps());
    }

    @Test
    void containsReflectsPresence() {
        SinglyLinkedList<String> list = new SinglyLinkedList<>();
        list.addLast("A");
        assertTrue(list.contains("A"));
        assertFalse(list.contains("B"));
    }

    @Test
    void clearEmptiesListAndAllowsReuse() {
        SinglyLinkedList<String> list = new SinglyLinkedList<>();
        list.addLast("A");
        list.addLast("B");
        list.clear();
        assertTrue(list.isEmpty());
        assertEquals(0, list.size());
        assertEquals(-1, list.search("A"));
        list.addLast("C"); // pastikan head dan tail sudah bersih
        assertEquals(1, list.size());
        assertEquals("C", list.get(0));
    }

    @Test
    void invariantHoldsAfterEveryOperation() {
        SinglyLinkedList<String> list = new SinglyLinkedList<>();
        list.validateInvariant();            // kosong
        list.addLast("B");
        list.validateInvariant();
        list.addFirst("A");
        list.validateInvariant();
        list.addLast("D");
        list.validateInvariant();
        list.insert(2, "C");
        list.validateInvariant();
        list.remove(0);                      // awal
        list.validateInvariant();
        list.remove(1);                      // tengah
        list.validateInvariant();
        list.remove(list.size() - 1);        // akhir
        list.validateInvariant();
        list.remove(0);                      // elemen terakhir
        list.validateInvariant();
        assertTrue(list.isEmpty());
        list.addLast("X");
        list.clear();
        list.validateInvariant();
    }

    @Test
    void worksThroughStrukturDataInterface() {
        StrukturData<String> data = new SinglyLinkedList<>();
        data.add("A");
        data.add("B");
        assertEquals(2, data.size());
        assertEquals("A", data.get(0));
        assertTrue(data.contains("B"));
        assertEquals("A", data.remove(0));
        data.clear();
        assertTrue(data.isEmpty());
    }
}