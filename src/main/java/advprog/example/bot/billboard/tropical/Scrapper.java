package advprog.example.bot.billboard.tropical;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import advprog.example.bot.billboard.tropical.objects.Song;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Scrapper {

    private static Scrapper instance = null;
    private Document billboard;
    public final static String TROPICAL_LINK = "https://www.billboard.com/charts/tropical-songs";

    private Scrapper() {
        try {
            billboard = Jsoup.connect(TROPICAL_LINK).get();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static Scrapper getInstance() {
        if(instance == null) {
            instance = new Scrapper();
        }
        return instance;
    }

    public Document getDocument() {
        return billboard;
    }

    public List<Song> getChart() {
        List<Song> songs = new LinkedList<>();
        Elements block = Scrapper.getInstance().getDocument()
                .select("div.chart-data");
        List<Element> container = block.select("article.chart-row");

        for (int i = 0; i < container.size() && i < 10; i++) {
            Elements text = container.get(i).select("div.chart-row__main-display");
            int number = Integer.parseInt(text.select("span.chart-row__current-week").text());
            String author = text.select(".chart-row__artist").text();
            String title = text.select("h2.chart-row__song").text();
            songs.add(new Song(number, author, title));
        }

        return songs;
    }
}

