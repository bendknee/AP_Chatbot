package advprog.example.bot.hotcountry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class SongTest {

    String url = "https://www.billboard.com/charts/country-songs";
    HotCountryBot hcb;

    @Before
    public void setUp() {
        hcb = new HotCountryBot();
        hcb.find = "Armik";
    }

    @Test
    public void getChartTest() {
        assertNotNull(hcb.getChart());
        for(HotCountrySong sng : hcb.chart) {
            sng.songData();
        }
    }

    @Test
    public void isExistTest() {
        assertTrue(hcb.isExist("Armik"));
    }

    @Test
    public void favTest() {
        assertEquals("Armik\nPacifica\n1", hcb.getFavArtist());
    }


    //TODO apakah menggunakan api yang benar
    @Test
    public void urlTest() {
        assertEquals(url, hcb.getUrl());
    }

}
