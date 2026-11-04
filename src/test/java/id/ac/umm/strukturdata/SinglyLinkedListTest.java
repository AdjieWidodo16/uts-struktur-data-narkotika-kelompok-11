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
}