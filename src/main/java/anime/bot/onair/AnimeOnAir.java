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
    private List<Anime> animeList;

    public AnimeOnAir(){
        animeList = new ArrayList<Anime>();
    }


    public void getAnimeOnAir(String url) {
        try {
            Document doc = Jsoup.connect(url).get();
            Elements element = doc.getElementsByClass("anime-card");
            for (Element e: element) {
                String title = e.getElementsByClass("main-title").html();
                List<String> genre = new ArrayList<>();
                Elements elementGenre = e.getElementsByClass("anime-tags");
                for (Element es : elementGenre) {
                    //System.out.println(es.select("li>a").html()+"hehe");
                    genre.add(es.select("li>a").html());
                }
                String synopsis = e.select(".anime-synopsis > p").html();

                Anime anime = new Anime(title, genre, synopsis);
                animeList.add(anime);
            }

        } catch (IOException e) {
            System.out.println("Url tidak ada");
        }
    }


    public String returnAnimeBasedOnGenreYearAndSeason(String genre){
        StringBuilder stringBuilder = new StringBuilder();
        String string = "Sorry but the specification you have entered was no longer in our Chart!";
        //String string = animeList.size()+"hehe";
        if (!animeList.isEmpty()){
            for (Anime anime : animeList) {
                if (anime.getGenre().toString().contains(genre)){
                    string = stringBuilder.append(format("%s\n", anime.toString())).toString();
                }
            }
        }
        return string;
    }

    public List<Anime> getAnimeList() {
        return animeList;
    }

    public void setAnimeList(List<Anime> animeList) {
        this.animeList = animeList;
    }

    public String returnAnime () {
        return "";
    }

}
