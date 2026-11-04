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
}