package advprog.example.bot.anime;

import jdk.nashorn.api.scripting.URLReader;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class AnimeBot {

    Anime anime;
    String url;

    public AnimeBot(String title) {
        String animeTitle = title.toLowerCase().replace(" ", "+");
        url = "https://myanimelist.net/api/anime/search.xml?q=" + animeTitle;
    }


    public void setUpAnime() {
        XMLReader myReader = XMLReaderFactory.createXMLReader();
        //myReader.setContentHandler(handler);
        myReader.parse(new InputSource(new URLReader(url).openStream()));
    }

    public String getStatusMessage() {
        return null;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
