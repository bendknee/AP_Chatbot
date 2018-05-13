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

        String replyText = contentText.replace("/echo", "");
        String[] findArtist = replyText.split(" ");
        if (findArtist.length >= 4 && (findArtist[1]+findArtist[2]).equalsIgnoreCase("billboardhot100")) {
            String nameArtistOrSong="";
            if (findArtist.length > 4) {
                for (int i = 3; i < findArtist.length; i++) {
                    if (i!= findArtist.length-1) {
                        nameArtistOrSong += findArtist[i] + " ";
                    } else {
                        nameArtistOrSong += findArtist[i];
                    }
                }

            } else {
                nameArtistOrSong = findArtist[3];
            }
            System.out.println(nameArtistOrSong);
            Hottest100 hottest100 = new Hottest100("https://www.billboard.com/charts/hot-100", nameArtistOrSong);
            toReply = hottest100.returnNameArtist();
            String replyToken = event.getReplyToken();
            reply(toReply, replyToken);
        } else {
            toReply = "Please Use a good input. E.g. /echo billboard hot100 [NAME OF ARTIST]";
            String replyToken = event.getReplyToken();
            reply(toReply, replyToken);
        }
        return new TextMessage(toReply);
    }

    @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));
    }

    private void reply(String replies, String token){
        Message textMessage = new TextMessage(replies);
        Message sticker = new StickerMessage("1", "137");
        try {
            List<Message> messageList = Arrays.asList(textMessage, sticker);
            lineMessagingClient.replyMessage(new ReplyMessage(token, messageList)).get();

        } catch (InterruptedException |ExecutionException e) {
            System.out.println("Error");
        }
    }
}
