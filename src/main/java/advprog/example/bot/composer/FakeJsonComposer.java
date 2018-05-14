package advprog.example.bot.composer;

import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;

import java.util.Random;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class FakeJsonComposer {

    private static final String API_URL = "https://jsonplaceholder.typicode.com/";
    private static final String[] JSON_TYPES = new String[] {
        "posts",
        "comments",
        "albums",
        "photos",
        "todos",
        "users"
    };

    public static Message composeMessage() {
        String reply = fetchFromApi();

        return new TextMessage(reply);
    }

    private static String fetchFromApi() {
        RestTemplate restTemplate = new RestTemplate();
        Random random = new Random();

        int jsonTypeIndex = random.nextInt(JSON_TYPES.length);
        int instanceId = random.nextInt(9) + 1;

        String uri = UriComponentsBuilder
                .fromUriString(API_URL + JSON_TYPES[jsonTypeIndex])
                .queryParam("id", instanceId)
                .toUriString();

        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);

        String responseText = "Something is wrong with our 3rd party server. "
                + "Please try again later.";
        if (response.getStatusCode().is2xxSuccessful()) {
            responseText = response.getBody();
        }

        return responseText;
    }
}
