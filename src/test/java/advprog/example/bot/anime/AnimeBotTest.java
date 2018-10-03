package advprog.example.bot.anime;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class AnimeBotTest {

    AnimeBot ab;

    @Before
    public void setUp() {
        ab = new AnimeBot("Akkun to Kanojo");
    }

    @Test
    public void getMessageTest() {
        assertEquals("ANIME is airing from 06-04-2018 until unknown time",ab.getStatusMessage());
    }

    @Test
    public void checkUrl() {
        assertEquals("https://myanimelist.net/api/anime/search.xml?q=akkun+to+kanojo", ab.getUrl());
    }


}
