package advprog.example.bot.controller;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import static java.lang.String.format;

public class ScrapperCDOriconSingle {
    /**
     * untuk scrapping ke web oricon
     * @param mode untuk memilih daily, weekly, monthly, atau yearly
     * @param date untuk memilih tanggal, bulan, tahun
     * @return String yang berupa list dari top 10 bluray di oricon
     */
    public static String scrapping(String mode, String date) {
        // Membuat offset web
        String offset;
        ArrayList<String> val = new ArrayList<>();
        if (mode.toLowerCase().equals("daily")) {
            offset = "https://www.oricon.co.jp/rank/js/d/"+date+"/";
        }
        else if (mode.toLowerCase().equals("weekly")) {
            offset = "https://www.oricon.co.jp/rank/js/w/"+date+"/";
        }
        else if (mode.toLowerCase().equals("monthly")) {
            offset = "https://www.oricon.co.jp/rank/js/m/"+date+"/";
        }
        else if (mode.toLowerCase().equals("yearly")) {
            offset = "https://www.oricon.co.jp/rank/js/y/"+date+"/";
        }
        else {
            return "Input Mode Salah!!!\n\n\n" +
                    "Format input\n" +
                    "/oricon jpsingles <YYYY | YYYY-MM | weekly YYYY-MM-DD | daily YYYY-MM-DD>";
        }

        // scrapping data
        try {
            Document dc = Jsoup.connect(offset).timeout(3000).get();
            Elements body = dc.select("section.box-rank-entry");

            int i = 1;
            for (Element rank : body) {
                String title = rank.select("h2.title").text();
                String artist = rank.select("p.name").text();
                Elements list = rank.select("ul.list li");
                String release = list.get(0).text();
                release = release.substring(5, 9) + "-" + release.substring(10, 12) + "-" + release.substring(13, 15);
                val.add(format("(%d) %s - %s - %s", i, title, artist, release));
                i++;
            }

            // return
            String top10 = "";
            for (String e : val) {
                top10 = top10 + e + "\n";
            }
            return top10;
        }
        catch (IOException e) {
            return "Input tanggal salah atau tidak ditemukan!!!\n\n\n" +
                    "Format input\n" +
                    "/oricon jpsingles <YYYY | YYYY-MM | weekly YYYY-MM-DD | daily YYYY-MM-DD>";
        }
    }
}
