package anime.bot.onair;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class AnimeOnAirTest {
    AnimeOnAir animeOnAir;
    AnimeOnAir animeOnAirFake;



    @Before
    public void setUp() {
        animeOnAir = new AnimeOnAir();
        animeOnAirFake = new AnimeOnAir();

        String url = "https://www.livechart.me/winter-2012/tv"; //masih dummy
        animeOnAir.getAnimeOnAir(url);

        /*String urlFake= "https://www.haha.com/";
        animeOnAirFake.getAnimeOnAir(urlFake);*/

    }


    @Test
    public void testAnimeAvailability() {
        List<Anime> anime = animeOnAir.getAnimeList();
        assertNotEquals(0, anime.size());
    }

    /*@Test
    public void testAnimeNotAvailable() {
        List<Anime> anime = animeOnAirFake.getAnimeList();
        assertEquals(0, anime.size());
    }*/

    @Test
    public void testOutput() {
        String fill = animeOnAir.returnAnimeBasedOnGenreYearAndSeason("Romance");
        assertTrue(fill.contains("High School"));

    }

    @Test
    public void testAnimeNotAvailable() {
        List<Anime> anime = animeOnAirFake.getAnimeList();
        assertEquals(0, anime.size());
    }

}

