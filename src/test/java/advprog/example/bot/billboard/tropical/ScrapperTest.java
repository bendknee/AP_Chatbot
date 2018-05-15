package advprog.example.bot.billboard.tropical;

import static org.junit.jupiter.api.Assertions.assertTrue;

import advprog.example.bot.billboard.tropical.objects.Song;

import java.io.IOException;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

public class ScrapperTest {

    List<Song> sample;
    Document doc;

    @Before
    public void setUp() throws IOException {
        System.out.println("Run Scrapper test...");
        sample = Scrapper.getInstance().getChart();
        doc = Jsoup.connect(Scrapper.TROPICAL_LINK).get();
    }

    @Test
    public void correctScrappingTest() throws IOException {
        System.out.println("Testing content...");
        String html = Jsoup.connect(Scrapper.TROPICAL_LINK).get().text();

        for (Song song : sample) {
            assertTrue(html.contains(song.getAuthor()));
            assertTrue(html.contains(Integer.toString(song.getRank())));
            assertTrue(html.contains(song.getSong()));
        }
    }

    @Test
    public void correctNumberOfChartTest() {
        System.out.println("Testing number of chart entries...");
        Elements nodes = doc.select("div.chart-data");
        int count = nodes.select("article.chart-row").size();

        assertTrue((count > sample.size() && sample.size() == 10) || (count == sample.size()));
    }

}
