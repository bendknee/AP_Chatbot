package advprog.example.bot.controller;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import java.util.logging.Logger;

@LineMessageHandler
public class EchoController {

    private static final Logger LOGGER = Logger.getLogger(EchoController.class.getName());

    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));
        TextMessageContent content = event.getMessage();
        String contentText = content.getText();

        String[] input = contentText.toLowerCase().split(" ");
        switch (input[0]) {
            case "/oricon":
                switch (input[1]) {
                    case "jpsingles":
                        if (! input[2].equals("daily") && ! input[2].equals("weekly")) {
                            String[] tmp = input[2].split("-");
                            if (tmp.length == 2) {
                                contentText = ScrappercdoriconSingle.scrapping("monthly", input[2]);
                            } else if (tmp.length == 1) {
                                contentText = ScrappercdoriconSingle.scrapping("yearly", input[2]);
                            }
                        } else {
                            contentText = ScrappercdoriconSingle.scrapping(input[2], input[3]);
                        }
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }

        String replyText = contentText.replace("/echo", "");
        return new TextMessage(replyText.substring(1));
    }

    @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));
    }
}
