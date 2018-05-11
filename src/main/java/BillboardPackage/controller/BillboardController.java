package BillboardPackage.controller.Controller;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class BillboardController {

    public static String cekArtis(String artis) throws IOException {
        Document doc = Jsoup.connect("https://www.billboard.com/charts/billboard-200").get();
        Elements containers = doc.select(".chart-row__artist");
        String hasil = "";
        for (int i = 0; i < 200; i++) {
            Element elements = containers.get(i);
            if (elements.text().equalsIgnoreCase(artis)) {
                hasil += elements.select(".chart-row__artist").text()+"\n"+
                        elements.select(".chart-row__song").text()+"\n" +"Position : "+(i+1)+"\n";
            }
        }
        return hasil;
    }
}
