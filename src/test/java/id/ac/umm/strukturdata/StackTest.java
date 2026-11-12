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

    private Stack<String> isiStack(String... items) {
        Stack<String> s = new Stack<>();
        for (String item : items) {
            s.push(item);
        }
        return s;
    }

    @Test
    void pushMenambahSize() {
        Stack<String> s = isiStack("a", "b", "c");
        assertEquals(3, s.size());
        assertFalse(s.isEmpty());
    }

    @Test
    void pushMelewatiKapasitasMenggandakanArray() {
        Stack<Integer> s = new Stack<>(2);
        s.push(1);
        s.push(2);
        assertEquals(2, s.capacity());
        s.push(3);
        assertEquals(4, s.capacity());
        assertEquals(3, s.size());
    }

    @Test
    void pushSeratusElemenTetapKonsisten() {
        Stack<Integer> s = new Stack<>(1);
        for (int i = 0; i < 100; i++) {
            s.push(i);
        }
        assertEquals(100, s.size());
        assertTrue(s.capacity() >= 100);
    }

    @Test
    void pushNullDitolakDanSizeTidakBerubah() {
        Stack<String> s = isiStack("a");
        assertThrows(IllegalArgumentException.class, () -> s.push(null));
        assertEquals(1, s.size());
    }

}