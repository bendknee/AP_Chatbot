package advprog.fakenews.bot.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

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

@SpringBootTest(properties = "line.bot.handler.enabled=false")
@ExtendWith(SpringExtension.class)
public class FakeNewsControllerTest {

    static {
        System.setProperty("line.bot.channelSecret", "SECRET");
        System.setProperty("line.bot.channelToken", "TOKEN");
    }

    @Autowired
    private FakeNewsController fakeNewsController;

    @Test
    void testContextLoads() {
        assertNotNull(fakeNewsController);
    }

    @Test
    void testHandleTextMessageEventSatire() {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("gue dapet di bignuggetnews.com loh");

        TextMessage reply = fakeNewsController.handleTextMessageEvent(event);

        assertEquals("is_satire", reply.getText());
    }

    @Test
    void testHandleTextMessageEventFake() {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/echo is_fake");

        TextMessage reply = fakeNewsController.handleTextMessageEvent(event);

        assertEquals("is_fake", reply.getText());
    }

    @Test
    void testHandleTextMessageEventConspiracy() {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/echo is_conspiracy");

        TextMessage reply = fakeNewsController.handleTextMessageEvent(event);

        assertEquals("is_conspiracy", reply.getText());
    }

    @Test
    void testHandleDefaultMessage() {
        Event event = mock(Event.class);

        fakeNewsController.handleDefaultMessage(event);

        verify(event, atLeastOnce()).getSource();
        verify(event, atLeastOnce()).getTimestamp();
    }
}