package advprog.example.bot.anime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class AnimeTest {

    Anime anime;

    @Before
    public void setUp() {
        anime = new Anime("My anime", "Mar 31, 2018", "Aug 22, 2018");
    }

    @Test
    public void nameTest(){
        anime.setTitle("My new Anime");
        assertEquals("My anime 2", anime.getTitle());
    }

    @Test
    public void isAiring(){
        anime.setAiringStatus();
        assertTrue(anime.isAiring());
    }



}
