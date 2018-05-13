package hot100.bot.hottests100;
import static java.lang.String.format;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ch.qos.logback.core.pattern.util.IEscapeUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

public class Hottest100 {
    private String billboardUrl;
    private List<Song> top50Hottest;
    private List<Song> top100Hottest;
    private List<Song> nameArtist;

    public Hottest100(String billboardUrl, String nameArtists) {
        this.billboardUrl = billboardUrl;
        top50Hottest = new ArrayList<>();
        top100Hottest = new ArrayList<>();
        nameArtist = new ArrayList<>();
        setTop100Hottest(billboardUrl, nameArtists);
    }

    private void setTop100Hottest(String url, String nameArtists) {
        try{
            Document doc = Jsoup.connect(url).get();
            Elements link = doc.getElementsByClass("chart-row");
            for (int i = 0; i < 100; i++) {
                Element elem = link.get(i);
                String artist = elem.getElementsByClass("chart-row__artist").html();
                String songs = elem.getElementsByClass("chart-row__song").html();

                String formatArtist = Parser.unescapeEntities(artist, false);
                String formatSongs = Parser.unescapeEntities(songs, false);

                Song song = new Song(formatArtist, formatSongs);

                if (nameArtists.equalsIgnoreCase(formatArtist)) {
                    this.nameArtist.add(song);

                }

                top100Hottest.add(song);
            }

        } catch (IOException e){
            System.out.println("IO EXCEPTION");
        }
    }

    public String returnNameArtist () {
        StringBuilder stringBuilder = new StringBuilder();
        String string = "Sorry but the artist you're looking for is not available in this chart!";
        int counter = 1;
        if (!nameArtist.isEmpty()){
            for (Song s : top100Hottest) {
                if (nameArtist.contains(s)) {
                    System.out.println(s.getName());
                    string = stringBuilder.append(format("(%d)\n %s\n", counter, s.toString())).toString();
                }
                counter++;
            }
        }
        return string;
    }


    /*public String printTo100List() {
        StringBuilder stringBuilder = new StringBuilder();
        int counters = 51;
        for (Song song : top100Hottest) {

            counters++;
        }
        return stringBuilder.toString();
    }*/

    public String getBillboardUrl(){
        return billboardUrl;
    }

    public List<Song> getTop100Hottest() {
        return top100Hottest;
    }
    public List<Song> getTop50Hottest() {
        return top50Hottest;
    }
}
