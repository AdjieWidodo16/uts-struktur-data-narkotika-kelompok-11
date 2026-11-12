package id.ac.umm.strukturdata;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class StackTest {

    @Test
    void stackBaruKosong() {
        Stack<String> s = new Stack<>();
        assertEquals(0, s.size());
        assertTrue(s.isEmpty());
    }

    @Test
    void kapasitasAwalDefaultSepuluh() {
        assertEquals(10, new Stack<String>().capacity());
    }

    @Test
    void kapasitasAwalKustom() {
        assertEquals(3, new Stack<String>(3).capacity());
    }

    @Test
    void kapasitasNolAtauNegatifDitolak() {
        assertThrows(IllegalArgumentException.class, () -> new Stack<String>(0));
        assertThrows(IllegalArgumentException.class, () -> new Stack<String>(-5));
    }
}