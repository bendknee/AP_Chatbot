package advprog.example.bot.composer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;

import java.io.IOException;

import java.util.Iterator;
import java.util.Locale;

import org.apache.commons.validator.routines.UrlValidator;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class LanguageDetectionComposer {
    private static final String BASE_API_URL = "https://api.dandelion.eu/datatxt/li/v1";
    private static final String API_TOKEN = "60f7f393c0cd43cbb579b08f95a57700";
    private static final String ERROR_5XX_MESSAGE = "Something is wrong with our 3rd party server. "
            + "Please try again later.";
    private static final UrlValidator URL_VALIDATOR = new UrlValidator();

    public static Message composeMessage(String arg) {
        String response = fetchFromApi(arg);
        String replyText = ERROR_5XX_MESSAGE;

        if (!ERROR_5XX_MESSAGE.equals(response)) {
            replyText = buildReplyText(response);
        }

        return new TextMessage(replyText);
    }

    private static String buildReplyText(String stringifiedJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        String replyText;

        try {
            JsonNode json = objectMapper.readTree(stringifiedJson);
            JsonNode detectedLangs = json.get("detectedLangs");
            StringBuilder stringBuilder = new StringBuilder();

            Iterator<JsonNode> iterator = detectedLangs.iterator();
            while (iterator.hasNext()) {
                JsonNode currentNode = iterator.next();
                String languageCode = currentNode.get("lang").asText();
                Locale locale = new Locale(languageCode.substring(0, 2));

                String countryName = locale.getDisplayLanguage();
                if (languageCode.length() > 2) {
                    countryName += languageCode.substring(2, languageCode.length());
                }

                int confidence = (int) (currentNode.get("confidence").asDouble() * 100);
                stringBuilder.append(countryName + " (" + confidence + "%)");

                if (iterator.hasNext()) {
                    stringBuilder.append("\n");
                }
            }

            replyText = stringBuilder.toString();
        } catch (IOException error) {
            replyText = "Something is wrong with our server. Please try again later";
        }

        return replyText;
    }

    private static String fetchFromApi(String arg) {
        RestTemplate restTemplate = new RestTemplate();
        String uri = buildApiUri(arg);

        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
        String responseText = ERROR_5XX_MESSAGE;
        if (response.getStatusCode().is2xxSuccessful()) {
            responseText = response.getBody();
        }

        return responseText;
    }

    private static String buildApiUri(String arg) {
        boolean isUrl = URL_VALIDATOR.isValid(arg) || URL_VALIDATOR.isValid("http://" + arg);
        String commandType = isUrl ? "url" : "text";

        return UriComponentsBuilder.fromUriString(BASE_API_URL)
                .queryParam("token", API_TOKEN)
                .queryParam(commandType, arg)
                .toUriString();
    }
}
