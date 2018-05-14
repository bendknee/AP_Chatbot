package advprog.example.bot.controller;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import org.springframework.web.client.RestTemplate;

import java.util.logging.Logger;

@LineMessageHandler
public class MusicBrainzController {

    private static final Logger LOGGER = Logger.getLogger(MusicBrainzController.class.getName());

    private static final String ROOT_API = "https://musicbrainz.org/ws/2/artist/?query=";

    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        // TODO: Implement this'
        LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));

        String replyText = "";

        String contentText = event.getMessage().getText();

        if (contentText.contains("/10album")) {
            String artistName = contentText.substring(8);
            RestTemplate restTemplate = new RestTemplate();

            replyText = restTemplate.getForObject(ROOT_API+artistName, String.class);

        }

        return null;
        //return new TextMessage(replyText);
    }

    @EventMapping
    public void handleDefaultMessage(Event event) {
        // TODO: Implement this
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));
    }

}
