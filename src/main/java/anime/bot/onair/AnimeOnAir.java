package anime.bot.onair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import static java.lang.String.format;
import java.io.IOException;
import ch.qos.logback.core.pattern.util.IEscapeUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;


public class AnimeOnAir {
    private String animeUrl;
    private List<Anime> animeList;

    public AnimeOnAir(String animeUrl){
        this.animeUrl = animeUrl;
        animeList = new ArrayList<Anime>();
    }

    public void getAnimeOnAir(String url) {
        try {
            Document doc = Jsoup.connect("https://www.livechart.me/spring-2018/tv").get();
            Elements element = doc.getElementsByClass("anime-card");
            for (Element e: element) {
                String title = e.getElementsByClass("main-title").html();
                List<String> genre = new ArrayList<>();
                Elements elementGenre = e.getElementsByClass("anime-tags");
                for (Element es : elementGenre) {
                    genre.add(es.html());
                }
                String synopsis = e.select(".anime-synopsis > p").html();

                Anime anime = new Anime(title, genre, synopsis);
                animeList.add(anime);

                System.out.println(title);
                System.out.println(synopsis);

            }

        } catch (IOException e) {
            System.out.println("Url tidak ada");
        }
    }

    public List<Anime> getAnimeList() {
        return animeList;
    }

    public void setAnimeList(List<Anime> animeList) {
        this.animeList = animeList;
    }


    public String getAnimeUrl() {
        return animeUrl;
    }

    public void setAnimeUrl(String animeUrl) {
        this.animeUrl = animeUrl;
    }



    private void getASetOfAnimes() {

    }

    public String returnAnime () {
        return "";
    }

}
