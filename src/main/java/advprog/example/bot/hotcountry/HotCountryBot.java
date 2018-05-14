package advprog.example.bot.hotcountry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;


public class HotCountryBot {

    public List<HotCountrySong> chart;
    public String find;
    public String url;

    public HotCountryBot() {
        this.url = "https://www.billboard.com/charts/country-songs";
        this.chart = new ArrayList<>();
        this.getChart();
    }

    public HotCountryBot(String find) {
        this.find = find;
        this.url = "https://www.billboard.com/charts/country-songs";
        this.chart = new ArrayList<>();
        this.getChart();
    }

    public List<HotCountrySong> getChart() {
        try {
            Document doc = Jsoup.connect(url).get();
            Elements data = doc.getElementsByClass("chart-row");
            for (int i = 0; i < 50; i++) {
                Element elem = data.get(i);
                String title = elem.getElementsByClass("chart-row__song").html();
                String artist = elem.getElementsByClass("chart-row__artist").html();

                String newTitle = Parser.unescapeEntities(title, false);
                String newArtist = Parser.unescapeEntities(artist, false);

                HotCountrySong alb = new HotCountrySong(newTitle, newArtist, i + 1);
                this.chart.add(alb);
            }
        } catch (IOException e) {
            System.out.println("IO EXCEPTION");
        }
        return this.chart;
    }

    public boolean isExist(String find) {
        for (HotCountrySong a : chart) {
            if (a.getArtist().toLowerCase().contains(find.toLowerCase())) {
                return true;
            }
        }

        return false;
    }

    public String getFavArtist() {
        if (isExist(find)) {
            for (HotCountrySong a : chart) {
                if (a.getArtist().toLowerCase().contains(find.toLowerCase())) {
                    return a.songData();
                }
            }
        }
        return "SORRY! Your favorite artist is not on the list!";
    }

    public String getUrl() {
        return this.url;
    }


}

