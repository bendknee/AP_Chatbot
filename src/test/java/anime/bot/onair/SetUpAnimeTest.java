package anime.bot.onair;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SetUpAnimeTest {
    SetUpAnime setUpAnime = new SetUpAnime();
    String url ="";
    @Before
    public void setUp() {
        url = "https://www.livechart.me/winter-2012/tv";
        setUpAnime.setYear("2012");
        setUpAnime.setSeason("winter");
        setUpAnime.setGenre("romance");
        setUpAnime.setUrl();
    }

    @Test
    public void testGetAnimeGenre() {
        assertEquals(setUpAnime.getUrl(), url);
    }

    @Test
    public void testGetYear() {
        assertEquals("2012", setUpAnime.getYear());
    }

    @Test
    public void testGetSeason() {
        assertEquals("winter", setUpAnime.getSeason());
    }

    @Test
    public void testGetGenre() {
        assertEquals("romance", setUpAnime.getGenre());
    }
}
