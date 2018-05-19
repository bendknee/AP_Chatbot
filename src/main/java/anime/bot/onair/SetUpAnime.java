package anime.bot.onair;

public class SetUpAnime {
    private String url;
    private String year;
    private String season;
    private String genre;

    public SetUpAnime() {
        this.year = year;
        this.season = season;
        this.genre = genre;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl() {
        this.url = "https://www.livechart.me/"+season+"-"+year+"/tv";;
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

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

}
