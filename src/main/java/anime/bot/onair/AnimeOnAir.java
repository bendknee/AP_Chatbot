package anime.bot.onair;

import java.util.ArrayList;
import java.util.List;

public class AnimeOnAir {
    private String animeUrl;
    private List<Anime> animeList;

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

    public AnimeOnAir(String animeUrl){
        this.animeUrl = animeUrl;
        animeList = new ArrayList<Anime>();
    }

    private void getASetOfAnimes() {

    }

    public String returnAnime () {
        return "";
    }

}
