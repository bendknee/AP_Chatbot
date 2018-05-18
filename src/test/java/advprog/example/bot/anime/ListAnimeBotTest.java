package advprog.example.bot.anime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;


public class ListAnimeBotTest {

    ListAnimeBot lab;

    @Before
    public  void setUp() {
        lab = new ListAnimeBot();
    }

    @Test
    public  void animeListTest() {
        String daftar = lab.getListAnime();
        assertNotNull(daftar);
    }

    @Test
    public void checkUrl() {
        assertEquals("https://www.livechart.me/schedule/tv", lab.getUrl());
    }
}
