package advprog.example.bot.billboard.tropical;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ScrapperTest {

    List<String> sample;
    Document doc;

    @Before
    public void setUp() throws IOException {
        System.out.println("Run Newscrapper test...");
        sample = Scrapper.getChart();
        doc = Jsoup.connect(Scrapper.TROPICAL_LINK).get();
    }

    @Test
    public void correctScrappingTest() throws IOException {
        System.out.println("Testing content...");
        String html = Jsoup.connect(Scrapper.TROPICAL_LINK).get().text();

        for (String head : sample) {
            assertTrue(html.contains(head));
        }
    }

    @Test
    public void correctNumberOfChartTest() {
        System.out.println("Testing number of chart entries...");
        Elements nodes = doc.select("div#site-news-forum");
        int count = 0;
        for (Element el : nodes.select("article.chart-row")) {
            count++;
        }
        assertEquals(count, sample.size());
    }

}
