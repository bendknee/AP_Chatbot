package anime.bot.onair;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class AnimeTest {
    Anime anime;

    @Before
    public void setUp() {
        anime = new Anime("Khatya The W", "romance", "2030", "Winter");
    }

    @Test
    public void getGenreTest() {
        String genre = anime.getGenre();
        assertEquals(genre, "romance");

    }

    @Test
    public void  getTitleTest() {
        String title = anime.getTitle();
        assertEquals(title, "Khatya The W");

    }

    @Test
    public void getYearTest() {
        String year = anime.getYear();
        assertEquals(year, "2030");

    }

    @Test
    public void seasonTest() {
        String season = anime.getSeason();
        assertEquals(season, "Winter");
    }

}
