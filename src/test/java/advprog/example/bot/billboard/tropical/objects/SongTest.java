package advprog.example.bot.billboard.tropical.objects;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SongTest {
    @Test
    public void toStringTest() {
        Song dummy = new Song(10, "hehe", "haha");
        assertEquals("(10) hehe - haha", dummy.toString());
    }
}
