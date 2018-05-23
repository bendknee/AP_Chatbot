package advprog.example.bot.feature;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest(properties = "line.bot.handler.enabled=false")
@ExtendWith(SpringExtension.class)

public class EnterkomputerTest {

    Enterkomputer enterkomputer = new Enterkomputer();

    static {
        System.setProperty("line.bot.channelSecret", "SECRET");
        System.setProperty("line.bot.channelToken", "TOKEN");
    }

    @Test
    void testContextLoads() {
        assertNotNull(enterkomputer);
    }

    @Test
    void testFindPriceFail() {
        assertEquals("Not found", Enterkomputer
                .findPrice("instrumen musik",("Yamaha Guitar S25.21 XB").toLowerCase()));
    }

    @Test
    void testFindPriceSuccess() {
        assertEquals("iGame nVidia Geforce GTX 1080 8GB DDR5X - "
                + "X-TOP-8G - Triple Fan ( Garansi 3 Bln ) - 9900000", Enterkomputer
                .findPrice(("VgA").toLowerCase(),("iGame Nvidia Geforce GTX 1080").toLowerCase()));
        assertEquals("No related items", Enterkomputer
                .findPrice("vga","bukan item"));
    }
}