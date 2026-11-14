package id.ac.umm.strukturdata;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Scanner;
import org.junit.jupiter.api.Test;

class InputHelperTest {

    private InputHelper dari(String teks) {
        return new InputHelper(new Scanner(
                new ByteArrayInputStream(teks.getBytes(StandardCharsets.UTF_8))));
    }

    @Test
    void bacaIntMengulangSampaiAngkaValid() {
        assertEquals(5, dari("abc\n\n5\n").bacaInt("> "));
    }

    @Test
    void bacaIntDalamMenolakDiLuarRentang() {
        assertEquals(3, dari("99\n-1\n3\n").bacaIntDalam("> ", 0, 10));
    }

    @Test
    void tanggalSalahDiulangSampaiValid() {
        assertEquals(LocalDate.of(2025, 1, 31),
                dari("31-01-2025\n2025-01-31\n").bacaTanggalOpsional("> "));
    }

    @Test
    void opsionalKosongMenghasilkanNolAtauNull() {
        InputHelper in = dari("\n\n\n\n");
        assertNull(in.bacaTeksOpsional("> "));
        assertNull(in.bacaTanggalOpsional("> "));
        assertEquals(0, in.bacaIntOpsional("> "));
        assertEquals(0.0, in.bacaDoubleOpsional("> "));
    }

    @Test
    void angkaNegatifDitolakLaluDiulang() {
        assertEquals(7, dari("-3\nabc\n7\n").bacaIntOpsional("> "));
    }

    @Test
    void inputHabisMelemparInputBerakhir() {
        assertThrows(InputHelper.InputBerakhirException.class,
                () -> dari("").bacaInt("> "));
    }
}