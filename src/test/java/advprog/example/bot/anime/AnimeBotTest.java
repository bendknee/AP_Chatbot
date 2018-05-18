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
        assertEquals("ANIME is airing from April,6 2018 until unknown",ab.getStatusMessage());
    }

    @Test
    public void checkUrl() {
        assertEquals("https://myanimelist.net/anime/36864/Akkun_to_Kanojo", ab.getUrl());
    }


}
