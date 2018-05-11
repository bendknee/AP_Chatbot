package advprog.example.bot.Hottest100;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import advprog.example.bot.hottest100.Hottest100;
import advprog.example.bot.hottest100.Song;
import org.junit.Before;
import org.junit.Test;

public class Hottest100Test {
    Hottest100 hottest100;
    String url = "https://www.billboard.com/charts/hot-100";

    @Before
    public void setUp() {
        hottest100 = new Hottest100(url);
    }

    @Test
    public void urlErrorTest() {
        new Hottest100("https://haha.com.id/");
    }

    @Test
    public void printTop100List() {
        String expectedOutput = hottest100.printTo100List();
        System.out.println(expectedOutput);
        assertTrue(expectedOutput.contains("Ariana"));
    }

    @Test
    public void getBillboardUrl() {
        assertEquals(url, hottest100.getBillboardUrl());
    }

    @Test
    public void getTopTenTropicalList() {
        List<Song> list = hottest100.getTop100Hottest();
        assertEquals(100, list.size());
    }
}
