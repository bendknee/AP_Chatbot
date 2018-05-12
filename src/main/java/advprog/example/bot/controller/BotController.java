package advprog.example.bot.controller;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.StickerMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import hot100.bot.hottests100.Hottest100;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
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
        String toReply= "test";
        String toReply1 = "test";

        String replyText = contentText.replace("/echo", "");
        switch (replyText.substring(1).toLowerCase()){
            case "billboard hot100 artist":
                Hottest100 hottest100 = new Hottest100("https://www.billboard.com/charts/hot-100");
                toReply = hottest100.printTo50List();
                toReply1 = hottest100.printTo100List();
                String replyToken = event.getReplyToken();
                reply(toReply, toReply1, replyToken);
                break;
            default:
                toReply = "Please Use a good input. E.g. /echo billboard hotcountry";
                replyToken = event.getReplyToken();
                reply(toReply, toReply1, replyToken);
                break;
        }
        return new TextMessage(toReply);
    }

    @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));
    }

    private void reply(String replies, String replies1, String token){
        Message textMessage = new TextMessage(replies);
        Message textMessages = new TextMessage(replies1);
        Message sticker = new StickerMessage("1", "137");
        try {
            List<Message> messageList = Arrays.asList(textMessage, textMessages, sticker);
            lineMessagingClient.replyMessage(new ReplyMessage(token, messageList)).get();

        } catch (InterruptedException |ExecutionException e) {
            System.out.println("Error");
        }
    }
}
