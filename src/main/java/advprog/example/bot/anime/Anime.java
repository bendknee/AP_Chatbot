package advprog.example.bot.anime;

import java.util.Date;

public class Anime {

    String title;
    Date start;
    Date end;
    boolean isAiring;

    public Anime(String title, String start, String end) {

    }

    public String getDetail() {
        return null;
    }

    public boolean setAiringStatus() {
        return true;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isAiring() {
        return isAiring;
    }

    public void setAiring(boolean airing) {
        isAiring = airing;
    }

}
