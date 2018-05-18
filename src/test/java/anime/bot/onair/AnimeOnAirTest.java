package anime.bot.onair;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AnimeOnAirTest {
    AnimeOnAir animeOnAir;
    String url = "animeheheheh.com"; //masih dummy

    @Before
    public void setUp() {
        animeOnAir = new AnimeOnAir(url);
    }


    @Test
    public void animeAvailability() {

    }


    @Test
    public void getUrl() {
        assertEquals(url, animeOnAir.getAnimeUrl());

    }

}

