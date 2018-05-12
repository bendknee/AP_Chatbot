package advprog.example.bot.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import advprog.example.bot.EventTestUtil;

import advprog.example.bot.controller.BotController;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest(properties = "line.bot.handler.enabled=false")
@ExtendWith(SpringExtension.class)
public class botControllerTest {
    final static String SECRET = "439c99086898c9e6e7cc04f4f51d2756";
    final static String TOKEN = "RUjvRQaT4k6l5WfOj8AY0w/DLEsLr70aTdfOAyyfUTI4sjsmEYqvBPFLAx57hVG1P9y80dd5TujlBesMhIS3gq09ybTPnIYU5Og1OSW/TUMUIhzIFJ35BYcNKcLR3HhYYO02Th2N78V3Fl2p5Pz8KQdB04t89/1O/w1cDnyilFU=";

    static {
        System.setProperty("line.bot.channelSecret", SECRET);
        System.setProperty("line.bot.channelToken", TOKEN);
    }

    @Autowired
    private BotController botController;

    @Test
    void testContextLoads() {
        assertNotNull(botController);
    }

    @Test
    void testHandleTextMessageEvent() {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/echo Lorem Ipsum");

        TextMessage reply = botController.handleTextMessageEvent(event);

        assertEquals("Lorem Ipsum", reply.getText());
    }

    @Test
    void testHandleDefaultMessage() {
        Event event = mock(Event.class);

        botController.handleDefaultMessage(event);

        verify(event, atLeastOnce()).getSource();
        verify(event, atLeastOnce()).getTimestamp();
    }

    @Test
    void testInswitch() {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/echo billboard hotcountry");
        TextMessage reply = botController.handleTextMessageEvent(event);
        assertNotNull(reply);

    }
}