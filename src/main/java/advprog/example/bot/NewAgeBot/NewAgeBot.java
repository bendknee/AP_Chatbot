package advprog.example.bot.NewAgeBot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

public class NewAgeBot {

    public List<NewAgeAlbum> chart;
    public String find;
    public String url;

    public NewAgeBot() {
        this.url = "https://www.billboard.com/charts/new-age-albums";
        this.chart = new ArrayList<>();
        this.getChart();
    }

    public List<NewAgeAlbum> getChart(){
        try {
            Document doc = Jsoup.connect(url).get();
            Elements data = doc.getElementsByClass("chart-row");
            for (int i = 0; i < 10; i++) {
                Element elem = data.get(i);
                String title = elem.getElementsByClass("chart-row__song").html();
                String artist = elem.getElementsByClass("chart-row__artist").html();

                String newTitle = Parser.unescapeEntities(title, false);
                String newArtist = Parser.unescapeEntities(artist, false);

                NewAgeAlbum alb = new NewAgeAlbum(newTitle, newArtist, i+1);
                this.chart.add(alb);
            }
        } catch (IOException e) {
            System.out.println("IO EXCEPTION");
        }
        return this.chart;
    }

    public boolean isExist(String find) {
        for(NewAgeAlbum a : chart) {
            if(a.getArtist().toLowerCase().contains(find.toLowerCase())) {
                return true;
            }
        }

        return false;
    }

    public String getFavArtist(){
        if (isExist(find)) {
            for (NewAgeAlbum a : chart) {
                if (a.getArtist().toLowerCase().contains(find.toLowerCase())) {
                    return a.albumData();
                }
            }
        }
        return "SORRY! Your favorite artist is not on the list!";
    }

    public String getUrl() {
        return this.url;
    }

}