package advprog.example.bot.command.mediawiki;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class MediaWiki {

    private String apiUrl;

    public MediaWiki(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public boolean isMediaWikiApiActive() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response;
        try {
            response = restTemplate.getForEntity(apiUrl, String.class);
        } catch (IllegalArgumentException e) {
            return false;
        }

        return response != null
                && response.getStatusCode() == HttpStatus.OK
                && response.getBody().contains("MediaWiki API");
    }

    public String getRandomPageUrl() {
        String url = apiUrl.substring(0, apiUrl.lastIndexOf("/")) + "/index.php/Special:Random";
        try {
            Connection.Response response = Jsoup.connect(url).followRedirects(true).execute();
            return response.url().toString();
        } catch (IOException e) {
            throw new IllegalStateException("Connection error");
        }
    }

    public String getPageTitle(String url) {
        String mainPageUrl = apiUrl.substring(0, apiUrl.lastIndexOf("/")) + "/index.php";
        int baseIndex;
        try {
            Connection.Response response = Jsoup.connect(mainPageUrl)
                    .followRedirects(true)
                    .execute();
            baseIndex = response.url().toString().lastIndexOf("/");
        } catch (IOException e) {
            throw new IllegalStateException("Connection error");
        }
        String title = url.substring(baseIndex + 1);
        System.out.println(title);
        try {
            return URLDecoder.decode(title.replace('_', ' '), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new IllegalStateException("Illegal encoding");
        }

    }
}
