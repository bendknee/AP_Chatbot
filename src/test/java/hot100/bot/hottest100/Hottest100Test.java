package hot100.bot.hottest100;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import hot100.bot.hottests100.Hottest100;
import hot100.bot.hottests100.Song;
import org.junit.Before;
import org.junit.Test;

public class Hottest100Test {
    Hottest100 hottest100;
    String url = "https://www.billboard.com/charts/hot-100";

    @Before
    public void setUp() {
        hottest100 = new Hottest100(url, "Drake");
    }

    @Test
    public void urlErrorTest() {
        new Hottest100("https://haha.com.id/", "hehe");
    }



    @Test
    public void getBillboardUrl() {
        assertEquals(url, hottest100.getBillboardUrl());
    }

    @Test
    public void getTop100SongList() {
        List<Song> list = hottest100.getTop100Hottest();
        assertEquals(100, list.size());
    }
}
