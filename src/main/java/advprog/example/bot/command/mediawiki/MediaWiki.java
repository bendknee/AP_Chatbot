package advprog.example.bot.command.mediawiki;

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

    private String getMainPageUrl() {
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.getForEntity(apiUrl + "/..", String.class);
        return "main page url"; // TODO: FIXTHIS
    }
}
