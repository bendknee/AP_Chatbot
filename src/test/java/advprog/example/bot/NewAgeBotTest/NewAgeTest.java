package advprog.example.bot.NewAgeBotTest;
import advprog.example.bot.NewAgeBot.NewAgeBot;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import org.junit.Before;
import org.junit.Test;



public class NewAgeTest {
    String url = "https://www.billboard.com/charts/new-age-albums";
    NewAgeBot nab;

    @Before
    public void setUp() {
        nab = new NewAgeBot();

    }

    @Test void getChartTest() {
        assertNotNull(nab.getChart());
    }

    @Test
    public void isExistTest() {
        assertTrue(nab.isExist()"Armik");
    }

    @Test
    public void favTest() {
        assertEquals("Armik\nPacifica\n1", nab.getFavArtist());
    }


    //TODO apakah menggunakan api yang benar
    @Test
    public void urlTest(){
        assertEquals(url, nab.getUrl());

    }

}
