package TextSimilarity.bot.controller;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import java.util.logging.Logger;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

@LineMessageHandler
public class TextSimilarityController {

    private static final Logger LOGGER = Logger.getLogger(TextSimilarityController
            .class.getName());

    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));

        TextMessageContent content = event.getMessage();
        String contentText = content.getText();
        String url = generateUrl(contentText);

        return new TextMessage(contentText);
    }

    @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));
    }

    public String generateUrl(String text) {
        text = text.replace("/echo /docs_sim ", "");
        String token = "08267f9bb04e40dc94f6181ddc9e56f4"; //NOTE! TOKEN CAN EXPIRED
        String url = "https://api.dandelion.eu/datatxt/sim/v1/?";

        if (text.contains("'")) {
            String[] arr = text.split("'");
            String text1 = arr[1].replace(" ", "%20");
            String text2 = arr[3].replace(" ", "%20");
            url += "text1=" + text1 + "&"
                    + "text2=" + text2 + "&"
                    + "token=" + token;
        }
        else {
            String[] arr = text.split(" ");
            String text1 = arr[0].replace(" ", "%20");
            String text2 = arr[1].replace(" ", "%20");
            url += "url1=" + text1 + "&"
                    + "url2=" + text2 + "&"
                    + "token=" + token;
        }

        return url;
    }
}