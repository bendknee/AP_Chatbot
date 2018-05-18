package advprog.example.bot.anime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class ListAnimeBotTest {

    ListAnimeBot lab;

    @Before
    public  void setUp() {
        lab = new ListAnimeBot();
    }

    @Test
    public  void animeListTest() {
        String daftar = lab.getListAnime();
        assertTrue(daftar.contains("Akkun to Kanojo"));
        assertTrue(daftar.contains("Wotaku ni Koi wa Muzukashii"));
        assertTrue(lab.getMapAnime().size() == 88);
    }
}
