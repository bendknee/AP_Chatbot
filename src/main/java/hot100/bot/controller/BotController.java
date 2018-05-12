package hot100.bot.controller;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import hot100.bot.hottests100.Hottest100;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

@LineMessageHandler
public class BotController {

    private static final Logger LOGGER = Logger.getLogger(BotController.class.getName());

    @Autowired
    LineMessagingClient lineMessagingClient;

    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));
        TextMessageContent content = event.getMessage();
        String contentText = content.getText();

        String replyText = contentText.replace("/echo", "");

        switch (replyText.substring(1)){
            case "billboard hot100 ARTIST":
                Hottest100 hottest100 = new Hottest100("https://www.billboard.com/charts/hot-100");
                String toReply = hottest100.printTo100List();
                String replyToken = event.getReplyToken();

                reply(toReply, replyToken);

        }

        return new TextMessage(replyText.substring(1));
    }

    @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));
    }

    private void reply(String replies, String token){
        TextMessage textMessage = new TextMessage(replies);
        try {
            lineMessagingClient.replyMessage(new ReplyMessage(token, textMessage)).get();
        } catch (InterruptedException |ExecutionException e) {
            System.out.println("Error");
        }
    }

}

