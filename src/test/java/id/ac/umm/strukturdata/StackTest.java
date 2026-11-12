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

    @Test
    void peekMelihatPuncakTanpaMenghapus() {
        Stack<String> s = isiStack("a", "b", "c");
        assertEquals("c", s.peek());
        assertEquals("c", s.peek());
        assertEquals(3, s.size());
    }

    @Test
    void popMengikutiUrutanLifo() {
        Stack<String> s = isiStack("a", "b", "c");
        assertEquals("c", s.pop());
        assertEquals("b", s.pop());
        assertEquals("a", s.pop());
        assertTrue(s.isEmpty());
    }

    @Test
    void urutanLifoTerjagaSetelahResize() {
        Stack<Integer> s = new Stack<>(2);
        for (int i = 1; i <= 25; i++) {
            s.push(i);
        }
        for (int i = 25; i >= 1; i--) {
            assertEquals(i, s.pop().intValue());
        }
    }

    @Test
    void popStackKosongMelemparExceptionDenganPesanJelas() {
        Stack<String> s = new Stack<>();
        IllegalStateException e = assertThrows(IllegalStateException.class, s::pop);
        assertTrue(e.getMessage().contains("kosong"));
    }

    @Test
    void peekStackKosongMelemparExceptionDenganPesanJelas() {
        Stack<String> s = new Stack<>();
        IllegalStateException e = assertThrows(IllegalStateException.class, s::peek);
        assertTrue(e.getMessage().contains("kosong"));
    }

    @Test
    void stackTetapKonsistenSetelahPopGagal() {
        Stack<String> s = isiStack("a");
        s.pop();
        assertThrows(IllegalStateException.class, s::pop);
        assertEquals(0, s.size());
        assertTrue(s.isEmpty());
        s.push("b");
        assertEquals("b", s.peek());
    }

    @Test
    void clearMengosongkanStack() {
        Stack<String> s = isiStack("a", "b", "c");
        s.clear();
        assertEquals(0, s.size());
        assertTrue(s.isEmpty());
        assertThrows(IllegalStateException.class, s::peek);
    }

    @Test
    void stackBisaDipakaiLagiSetelahClear() {
        Stack<String> s = isiStack("a", "b");
        s.clear();
        s.push("x");
        assertEquals("x", s.peek());
        assertEquals(1, s.size());
    }

    @Test
    void containsMenemukanElemenAda() {
        Stack<String> s = isiStack("a", "b", "c");
        assertTrue(s.contains("a"));
        assertTrue(s.contains("c"));
    }

    @Test
    void containsFalseUntukElemenTidakAdaDanStackKosong() {
        assertFalse(isiStack("a", "b").contains("z"));
        assertFalse(new Stack<String>().contains("a"));
    }

    @Test
    void containsTidakMengubahStack() {
        Stack<String> s = isiStack("a", "b", "c");
        s.contains("a");
        assertEquals(3, s.size());
        assertEquals("c", s.peek());
    }



}