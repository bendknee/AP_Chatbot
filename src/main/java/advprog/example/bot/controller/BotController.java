package advprog.example.bot.controller;

import advprog.example.bot.hottest100.Hottest100;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

@LineMessageHandler
public class BotController {

    private static final Logger LOGGER = Logger.getLogger(BotController.class.getName());

    @Autowired
    private LineMessagingClient lineMessagingClient;

    @EventMapping
    public void handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        String hottests100="";
        LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));
        TextMessageContent content = event.getMessage();
        String contentText = content.getText();

        String replyText = contentText.replace("/echo", "");

        switch (replyText.substring(1)) {
            case "billboard hot100 ARTIS":
                Hottest100 hottest100 = new Hottest100("https://www.billboard.com/charts/hot-100");
                hottests100 = hottest100.printTo100List();
                String replyToken = event.getReplyToken();
                replyChat(replyToken, hottests100);
                break;
        }
    }

    @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));
    }

    private void replyChat(String replyToken, String ans) {
        TextMessage answer = new TextMessage(ans);
        try {
            lineMessagingClient.replyMessage(new ReplyMessage(replyToken, answer)).get();

        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Error");
        }
    }
}

