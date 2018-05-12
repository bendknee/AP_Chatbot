package hot100.bot.hottest100;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import hot100.bot.hottests100.Hottest100;
import hot100.bot.hottests100.Song;
import org.junit.Before;
import org.junit.Test;

public class SongTest {
    Song song;

    @Before
    public void setUp() {
        song = new Song("Zulia");
    }

    @Test
    public void returnArtistTest() {
        String artist = song.getArtists();
        assertEquals(artist, "Zulia");
    }

    @Test
    public void setNameTest() {
        song.setArtists("Delfa");
        String artistNew = song.getArtists();
        assertEquals(artistNew, "Delfa");
    }
}
