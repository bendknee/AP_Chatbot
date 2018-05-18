package anime.bot.onair;

import java.util.ArrayList;
import java.util.List;

public class Anime {
    List<String> genre;
    private String title;
    private String synopsis;

    public Anime(String title, List<String> genre, String synopsis){
        this.title = title;
        this.genre = genre;
        this.synopsis = synopsis;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
