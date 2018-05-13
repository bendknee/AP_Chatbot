package advprog.example.bot.controller;

import advprog.example.bot.EventTestUtil;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static advprog.example.bot.EventTestUtil.createDummyTextMessage;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest(properties = "line.bot.handler.enabled=false")
@ExtendWith(SpringExtension.class)
public class BotControllerTest {

    static {
        System.setProperty("line.bot.channelSecret", "SECRET");
        System.setProperty("line.bot.channelToken", "TOKEN");
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
                createDummyTextMessage("/echo Lorem Ipsum");

        TextMessage reply = botController.handleTextMessageEvent(event);

        assertEquals("Please Use a good input. E.g. /echo billboard hot100 [NAME OF ARTIST]", reply.getText());
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
                EventTestUtil.createDummyTextMessage("/echo billboard hot100 Ed Sheeran");
        TextMessage reply = botController.handleTextMessageEvent(event);
        assertNotNull(reply);

    }
}