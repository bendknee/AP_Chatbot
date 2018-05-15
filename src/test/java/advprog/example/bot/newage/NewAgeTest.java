package advprog.example.bot.newage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;



public class NewAgeTest {
    String url = "https://www.billboard.com/charts/new-age-albums";
    NewAgeBot nab;

    @Before
    public void setUp() {
        nab = new NewAgeBot();
        nab.find = "Armik";
    }

    @Test
    public void getChartTest() {
        assertNotNull(nab.getChart());
        for (NewAgeAlbum alb : nab.chart) {
            alb.albumData();
        }
    }

    @Test
    public void isExistTest() {
        assertTrue(nab.isExist("Armik"));
    }

    @Test
    public void favTest() {
        assertEquals("Armik\nPacifica\n1\n\nArmik\nEnamor\n3", nab.getFavArtist());
    }


    //TODO apakah menggunakan api yang benar
    @Test
    public void urlTest() {
        assertEquals(url, nab.getUrl());

    }

}
