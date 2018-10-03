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
        anime = new Anime("My anime", "2018-03-01", "2018-08-22");
    }

    @Test
    public void nameTest() {
        anime.setTitle("My anime 2");
        assertEquals("My anime 2", anime.getTitle());
    }


}
