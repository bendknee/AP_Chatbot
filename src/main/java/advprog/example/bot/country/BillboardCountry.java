package advprog.example.bot.country;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;


public class BillboardCountry {
    private String billboardUrl;
    private List<Song> topTenCountryList;

    public BillboardCountry(String billboardUrl) {
        this.billboardUrl = billboardUrl;
        topTenCountryList = new ArrayList<Song>();
    }

}
