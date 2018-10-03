package advprog.example.bot.anime;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

public class ListAnimeBot {

    HashMap<String, Integer> list;
    String url = "https://www.livechart.me/schedule/tv";

    public ListAnimeBot() {
        list = new HashMap<String, Integer>();
        setUpAllAnime();
    }

    public void setUpAllAnime() {
        try {
            Document doc = Jsoup.connect(url).get();
            Elements data = doc.select("div.schedule-card");
            for (int i = 0; i < data.size(); i++) {
                Element elem = data.get(i);
                String title = elem.select("div.schedule-card-title").html();
                String eps = elem.select("div.schedule-card-countdown").html();

                String newTitle = Parser.unescapeEntities(title, false);
                String newEps = Parser.unescapeEntities(eps, false);


                String[] tempForEp = newEps.split(":");
                String epNum = tempForEp[0];
                epNum = epNum.replace("EP", "");

                String[] tempForTime = tempForEp[1].split("\"");
                String timestamp = tempForTime[1];

                SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
                sf.setTimeZone(TimeZone.getTimeZone("Japan"));

                Date date = new Date(Long.parseLong(timestamp) * 1000);

                String airingDate = sf.format(date);

                if (airingDate.equals(sf.format(new Date()))) {
                    list.put(newTitle,Integer.parseInt(epNum));
                }
            }
        } catch (IOException e) {
            System.out.println("IO EXCEPTION");
        }

    }

    public String getListAnime() {
        StringBuilder sb = new StringBuilder();
        if (!list.isEmpty()) {
            for (String name : list.keySet()) {
                String key = name.toString();
                String value = list.get(name).toString();
                if (sb.toString().equals("")) {
                    sb.append(key + " " + value);
                } else {
                    sb.append("\n" + key + " " + value);
                }
            }
            return sb.toString();
        }
        return "Ooops! No anime is airing today.";
    }

    public String getUrl() {
        return url;
    }

}
