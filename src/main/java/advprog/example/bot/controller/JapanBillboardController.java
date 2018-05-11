package advprog.billboard.japan.controller;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import java.util.logging.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

@LineMessageHandler
public class JapanBillboardController {

    private static final Logger LOGGER = Logger.getLogger(JapanBillboardController.class.getName());
    private static final String[] jsonRequest;

    static {
        jsonRequest = new String[]{"posts", "comments", "albums", "photos", "todos", "user"};
    }

    private static final RestTemplate restTemplate = new RestTemplate();

    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));
        TextMessageContent content = event.getMessage();
        try {
            if (!content.getText().equals("/billboard japan100")) {
                throw new IllegalArgumentException();
            }
            String replyText = getJsonObject();
            //replyText = restTemplate.getForObject(ApiCommand.getCommand(), String.class);
            return new TextMessage(replyText);
        } catch (IllegalArgumentException e) {
            return new TextMessage("Command not found for " + content.getText());
        }


    }

    @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));
    }

    @GetMapping
    public String getJsonObject() {
        return "(1) Me - Always Me";
    }
}
