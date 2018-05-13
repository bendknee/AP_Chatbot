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
public class FakeJsonController {

    private static final Logger LOGGER = Logger.getLogger(FakeJsonController.class.getName());
    private static final String API_URL = "https://jsonplaceholder.typicode.com/posts?userId=1";

    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        String replyText = "Something happened. Please try again.";
        String command = event.getMessage().getText();

        if (command != null && command.matches("^/fake_json$")) {
            RestTemplate restTemplate = new RestTemplate();

            String response = restTemplate.getForObject(API_URL, String.class);

            if (response != null) {
                replyText = response;
            }
        }

        return new TextMessage(replyText);
    }

    @EventMapping
    public void handleDefaultMessage(Event event) {
        // TODO : Implement this
    }
}

