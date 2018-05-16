package advprog.example.bot.controller;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

import java.util.logging.Logger;
import java.io.IOException;

@LineMessageHandler
public class YoutubeInfoController {

    private static final Logger LOGGER = Logger.getLogger(YoutubeInfoController.class.getName());

    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) throws IOException {
        LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));
        TextMessageContent content = event.getMessage();
        String contentText = content.getText();

        if (contentText.contains("/youtube ")){
            String replyText = contentText.replace("/youtube", "");
            Document doc = Jsoup.connect(replyText).header("User-Agent", "Chrome").get();
            Element body = doc.body();
            String videoTitle = body.getElementById("eow-title").attr("title");
            String channelName = body.getElementById("watch7-user-header").getElementsByClass("yt-user-info").get(0).child(0).wholeText();
            String noOfLikes = body.getElementsByAttributeValue("title", "I like this").get(0).text();
            String noOfDislikes = body.getElementsByAttributeValue("title", "I dislike this").get(0).text();
//            VideoData videoData = new VideoData(videoTitle,channelName,noOfLikes,noOfDislikes);
            return new TextMessage("Title: " + videoTitle + '\n\n' + "Channel: " + channelName + '\n\n' + "Number of Likes: " + noOfLikes + '\n\n' + "Number of Dislikes: " + noOfDislikes);
        }
        String error = "Sorry, your Command is wrong, Try /youtube [Youtube URL]";
        return new TextMessage(error);
    }

    @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));
    }

}
