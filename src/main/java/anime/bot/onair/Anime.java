package anime.bot.onair;

import java.util.ArrayList;
import java.util.List;

public class Anime {
    List<String> genre;
    private String title;
    private String synopsis;

    public List<String> getGenre() {
        return genre;
    }


    public String getSynopsis() {
        return synopsis;
    }

    public Anime(String title, List<String> genre, String synopsis) {
        this.title = title;
        this.genre = genre;
        this.synopsis = synopsis;
    }


    public String getTitle() {
        return title;
    }


    @Override
    public String toString() {
        return title + " \n " + synopsis;
    }

}
