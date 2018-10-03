package advprog.example.bot.anime;

import static javax.xml.bind.DatatypeConverter.printBase64Binary;

import java.net.URL;
import java.text.SimpleDateFormat;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


public class AnimeBot {

    String title;
    Anime anime;
    String url;
    SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy");

    public AnimeBot(String title) {
        System.setProperty("http.agent", "Chrome");
        title = title;
        String animeTitle = title.toLowerCase().replace(" ", "+");
        url = "https://myanimelist.net/api/anime/search.xml?q=" + animeTitle;
        setUpAnime();
    }


    public void setUpAnime() {
        try {

            URL uri = new URL(url);

            String auth = "khty31:Khatya";
            String basicAuth = "Basic " + printBase64Binary(auth.getBytes());
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", basicAuth);
            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate
                    .exchange(url, HttpMethod.GET, entity, String.class);
            String str = response.getBody();

            //System.out.println(xmlString);

            if (str != null) {
                String title = str.split("\n")[4].replace("<title>", "")
                        .replace("</title>", "")
                        .replace("    ", "");
                String startDate = str.split("\n")[11].replace("<start_date>", "")
                        .replace("</start_date>", "")
                        .replace("    ", "");
                String finishDate = str.split("\n")[12].replace("<end_date>", "")
                        .replace("</end_date>", "")
                        .replace("    ", "");

                anime = new Anime(title, startDate, finishDate);

            }


        } catch (Exception ex) {
            System.out.println("unable to load XML in : " + ex);
        }
    }


    public String getStatusMessage() {
        if (anime != null) {
            return anime.getStatusMessage();
        }
        return "Sorry! No anime found";
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }




}
