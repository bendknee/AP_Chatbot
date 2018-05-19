package anime.bot.onair;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class AnimeTest {
    Anime anime;

    @Before
    public void setUp() {
        List<String> genre = new ArrayList<>();
        genre.add("Romance");
        anime = new Anime("Khatya The W", genre, "Story about a girl who likes Miku");
    }


    @Test
    public void  getTitleTest() {
        String title = anime.getTitle();
        assertEquals(title, "Khatya The W");
    }

    @Test
    public void getSynopsis() {
        String synopsis = anime.getSynopsis();
        assertEquals("Story about a girl who likes Miku", synopsis);
    }





}
