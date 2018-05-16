package advprog.nsfw.bot;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.ImageMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.source.UserSource;

import java.time.Instant;

public class EventTestUtil {

    private EventTestUtil() {
        // Default private constructor
    }

    public static MessageEvent<TextMessageContent> createDummyTextMessage(String text) {
        return new MessageEvent<>("replyToken", new UserSource("userId"),
                new TextMessageContent("id", text),
                Instant.parse("2018-01-01T00:00:00.000Z"));
    }

    public static MessageEvent<ImageMessageContent> createDummyImageMessage(){
        return new MessageEvent<>("replyToken", new UserSource("userId"),
                new ImageMessageContent("7961252566675"),Instant.parse("2018-01-01T00:00:00.000Z"));
                //new ImageMessageContent("id")),Instant.parse("2018-01-01T00:00:00.000Z");
    }
}
