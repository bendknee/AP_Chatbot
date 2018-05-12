package country.bot.top10countrysong;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class SongTest {
    Song song;

    @Before
    public void setUp() {
        song = new Song("Potato Head", "Zulia");
    }

    @Test
    public void returnArtistTest() {
        String artist = song.getArtist();
        assertEquals(artist, "Zulia");
    }

    @Test
    public void returnSong() {
        String songs = song.getName();
        assertEquals(songs, "Potato Head");
    }

    @Test
    public void setSongTest() {
        song.setName("Wkwk");
        String songNew = song.getName();
        assertEquals(songNew, "Wkwk");
    }

    @Test
    public void setNameTest() {
        song.setArtist("Delfa");
        String artistNew = song.getArtist();
        assertEquals(artistNew, "Delfa");
    }
}
