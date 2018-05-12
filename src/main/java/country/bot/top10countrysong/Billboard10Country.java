package country.bot.top10countrysong;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;


public class Billboard10Country {
    private String billboardUrl;
    private List<Song> topTenCountryList;

    public Billboard10Country(String billboardUrl) {
        this.billboardUrl = billboardUrl;
        topTenCountryList = new ArrayList<Song>();
        setTopTenCountrySongs(billboardUrl);
    }

    private void setTopTenCountrySongs(String url) {
        try {
            Document docs = Jsoup.connect(url).get();
            Elements links = docs.getElementsByClass("chart-row");
            for (int i = 0; i < 10; i++) {
                Element elem = links.get(i);
                String name = elem.getElementsByClass("chart-row__song").html();
                String artist = elem.getElementsByClass("chart-row__artist").html();

                String formattedNames = Parser.unescapeEntities(name, false);
                String formattedArtists = Parser.unescapeEntities(artist, false);

                Song song = new Song(formattedNames, formattedArtists);
                topTenCountryList.add(song);

            }
        } catch (IOException e) {
            System.out.println("Illegal IO");
        }
    }

    public String printTopTentList() {
        StringBuilder stringBuilder = new StringBuilder();
        int counter = 1;
        for (Song s: topTenCountryList) {
            stringBuilder.append(format("(%d) %s\n", counter, s.toString()));
            counter++;
        }
        return stringBuilder.toString();
    }

    public String getBillboardUrl() {
        return billboardUrl;
    }

    public List<Song> getTopTenCountryList() {
        return topTenCountryList;
    }
}
