package anime.bot.onair;

public class Anime {
    private String genre;
    private String title;
    private String year;
    private String season;

    public Anime(String title, String genre, String year, String season){
        this.title = title;
        this.genre = genre;
        this.year = year;
        this.season = season;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }
}
