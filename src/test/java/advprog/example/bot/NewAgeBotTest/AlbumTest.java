package advprog.example.bot.NewAgeBotTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import advprog.example.bot.NewAgeBot.NewAgeAlbum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AlbumTest {

    NewAgeAlbum album;

    @BeforeEach
    void setUp() {
        album = new NewAgeAlbum("My album", "My artist", 3);
    }

    @Test
    void titleTest() {
        assertEquals("My album", album.getTitle());
        album.setTitle("My album2");
        assertEquals("My album2", album.getTitle());
    }


    @Test
    void artisTest() {
        assertEquals("My artist", album.getArtist());
        album.setArtist("My artist2");
        assertEquals("My artist2", album.getArtist());
    }

    @Test
    void rankTest() {
        assertEquals(3, album.getRank());
        album.setRank(2);
        assertEquals(2, album.getRank());
    }

    @Test
    void albumDataTest() {
        assertEquals("My artist\nMy album\n3"  , album.albumData());
    }
}
