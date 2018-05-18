package advprog.example.bot;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.source.GroupSource;
import com.linecorp.bot.model.event.source.UserSource;

import java.time.Instant;

public class EventTestUtil {

    private EventTestUtil() {
        // Default private constructor
    }

    public static MessageEvent<TextMessageContent> createDummyTextMessagePrivateChat(String text) {
        return new MessageEvent<>("replyToken", new UserSource("U1"),
                new TextMessageContent("id", text),
                Instant.parse("2018-01-01T00:00:00.000Z"));
    }

    public static MessageEvent<TextMessageContent> createDummyTextMessageGroupChat(String text) {
        return new MessageEvent<>("replyToken", new GroupSource("C1", "U1"),
                new TextMessageContent("id", text),
                Instant.parse("2018-01-01T00:00:00.000Z"));
    }
}
