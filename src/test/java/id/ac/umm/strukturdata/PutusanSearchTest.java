package id.ac.umm.strukturdata;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PutusanSearchTest {

    @Test
    void hasilTidakDitemukanMenyimpanPosisiDanLangkah() {
        PutusanSearch.Hasil h = new PutusanSearch.Hasil(null, -1, 3);
        assertFalse(h.isDitemukan());
        assertNull(h.getPutusan());
        assertEquals(-1, h.getPosisi());
        assertEquals(3, h.getLangkah());
    }
}