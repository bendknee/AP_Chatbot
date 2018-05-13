package advprog.example.bot;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.source.RoomSource;

import java.time.Instant;

public class EventTestUtil {

    private EventTestUtil() {
        // Default private constructor
    }

    public static MessageEvent<TextMessageContent> createDummyTextMessage(String text) {
        return new MessageEvent<>("replyToken", new RoomSource("U1", "R1"),
                new TextMessageContent("id", text),
                Instant.parse("2018-01-01T00:00:00.000Z"));
    }
}
