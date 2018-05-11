package advprog.example.bot;

import com.linecorp.bot.model.event.MessageEvent; 
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.source.UserSource;

import java.time.Instant;

public class BotTropicalTest {

    private BotTropicalTest() {
        // Default private constructor
    }

    public static MessageEvent<TextMessageContent> createDummyTextMessage(String text) {
        return new MessageEvent<>("replyToken", new UserSource("userId"),
                new TextMessageContent("id", text),
                Instant.parse("2018-01-01T00:00:00.000Z"));
    }
    @Test
    public void testErrorMessage() {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/hahaha");

        TextMessage reply = BotTropicalTest.handleTextMessageEvent(event);
        assertEquals("Command not found for /hahaha", reply.getText());
    }
    @Test
    public void testHandleTextMessageEvent() {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/billboard tropical100");

        TextMessage reply = BotTropicalTest.handleTextMessageEvent(event);

        assertEquals(reply.getText(), "(1) Me - Always Me");
    }
    @Test
    public void testHandleDefaultMessage() {
        Event event = mock(Event.class);

        BotTropicalTest.handleDefaultMessage(event);

        verify(event, atLeastOnce()).getSource();
        verify(event, atLeastOnce()).getTimestamp();
    }

    @Test
    public void applicationContextLoaded() {
    }

    @Test
    public void applicationContextTest() {
    	BotTropicalTest.main(new String[] {});
    }
}
