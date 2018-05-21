package advprog.example.bot.command.mediawiki;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class Wiki {

    private String name;
    private String apiUrl;

    public Wiki(String apiUrl) {
        this.apiUrl = apiUrl;
        this.name = getWikiName();
    }

    public String getName() {
        return name;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public String getWikiName() {
        return "Wiki name";
    }
}
