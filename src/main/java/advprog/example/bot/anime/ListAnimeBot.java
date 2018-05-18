package advprog.example.bot.anime;

import java.util.HashMap;
import java.util.Map;

public class ListAnimeBot {

    HashMap<String, Integer> list;
    String url = "https://www.livechart.me/spring-2018/tv";

    public ListAnimeBot() {
        list = new HashMap<String, Integer>();
        setUpAllAnime();
    }

    public void setUpAllAnime() {

    }

    public Map<String, Integer> getMapAnime() {
        return list;
    }

    public String getListAnime() {
        return null;
    }

}
