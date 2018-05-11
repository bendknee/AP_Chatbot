package advprog.example.bot.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.logging.Logger;

@LineMessageHandler
public class LanguageDetectionController {
    private static final Logger LOGGER = Logger.getLogger(LanguageDetectionController.class.getName());
    private static final String BASE_API_URL = "https://api.dandelion.eu/datatxt/li/v1";
    private static final String API_TOKEN = "60f7f393c0cd43cbb579b08f95a57700";
    private static final String COMMAND_REGEX = "^/detect_lang .*";
    private static final UrlValidator URL_VALIDATOR = new UrlValidator() {
        @Override
        public boolean isValid(String value) {
            return super.isValid(value)
                    || super.isValid("https://" + value)
                    || super.isValid("http://" + value);
        }
    };


    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        String command = event.getMessage().getText();

        if (command.matches(COMMAND_REGEX)) {
            command = command.replace("/detect_lang ", "");
            String commandType = URL_VALIDATOR.isValid(command) ? "url" : "text";

            UriComponentsBuilder uriBuilder = UriComponentsBuilder
                    .fromUriString(BASE_API_URL)
                    .queryParam("token", API_TOKEN)
                    .queryParam(commandType, command);

            RestTemplate restTemplate = new RestTemplate();
            Response response = restTemplate.getForObject(uriBuilder.toUriString(), Response.class);

            return new TextMessage(response.getDetectedLangs()[0].getLang() + " " + response.getDetectedLangs()[0].getConfidence());
        }

        return new TextMessage("nope");
    }

    @EventMapping
    public void handleDefaultMessage(Event event) {
        // TODO : Implement this
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class Response {
    private Language[] detectedLangs;

    public Language[] getDetectedLangs() {
        return detectedLangs;
    }

    public void setDetectedLangs(Language[] detectedLangs) {
        this.detectedLangs = detectedLangs;
    }
}

class Language {
    private String lang;
    private double confidence;

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }
}
