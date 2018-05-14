package advprog.example.bot.hotcountry;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;


public class SongTest {

    HotCountrySong song;

    @BeforeEach
    public void setUp() {
        song = new HotCountrySong("My song", "My artist", 3);
    }

    @org.junit.jupiter.api.Test
    public void titleTest() {
        Assertions.assertEquals("My song", song.getTitle());
        song.setTitle("My song2");
        Assertions.assertEquals("My song2", song.getTitle());
    }


    @org.junit.jupiter.api.Test
    public void artisTest() {
        Assertions.assertEquals("My artist", song.getArtist());
        song.setArtist("My artist2");
        Assertions.assertEquals("My artist2", song.getArtist());
    }

    @org.junit.jupiter.api.Test
    public void rankTest() {
        Assertions.assertEquals(3, song.getRank());
        song.setRank(2);
        Assertions.assertEquals(2, song.getRank());
    }

    @org.junit.jupiter.api.Test
    public void songDataTest() {
        Assertions.assertEquals("My artist\nMy song\n3"  , song.songData());
    }

}
