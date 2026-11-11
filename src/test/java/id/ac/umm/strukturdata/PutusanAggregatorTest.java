package id.ac.umm.strukturdata;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PutusanAggregatorTest {

    @Test
    void jenisCountMenyimpanJenisDanBertambah() {
        PutusanAggregator.JenisCount c = new PutusanAggregator.JenisCount("Sabu", 1);
        assertEquals("Sabu", c.getJenis());
        assertEquals(1, c.getJumlah());
        c.tambah();
        assertEquals(2, c.getJumlah());
    }
}