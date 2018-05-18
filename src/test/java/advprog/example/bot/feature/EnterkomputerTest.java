package advprog.example.bot.feature;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EnterkomputerTest {
    @Test
    public void findPriceTest(){
        assertEquals("name desc price",Enterkomputer.findPrice());
    }
}