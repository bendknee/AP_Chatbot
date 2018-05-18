package advprog.example.bot.feature;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EnterkomputerTest {
    @Test
    public void findPriceTest(){
        String category = "VGA";
        String name = "Geforce GTX 1080";
        assertEquals("name desc price",Enterkomputer.findPrice(category, name));
    }
}