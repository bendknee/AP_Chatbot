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
    void testHandleTextMessageEventSafe() {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/is_satire pornhub.com");

        TextMessage reply = fakeNewsController.handleTextMessageEvent(event);

        assertEquals("pornhub.com is a safe news website", reply.getText());
    }

    @Test
    void testHandleTextMessageEventSatire() {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/is_satire conservativespirit.com");

        TextMessage reply = fakeNewsController.handleTextMessageEvent(event);

        assertEquals("conservativespirit.com isn't a satire news website", reply.getText());
    }

    @Test
    void testHandleTextMessageEventFake() {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/is_fake conservativespirit.com");

        TextMessage reply = fakeNewsController.handleTextMessageEvent(event);

        assertEquals("conservativespirit.com is a fake news website", reply.getText());
    }

    @Test
    void testHandleTextMessageEventConspiracy() {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/is_conspiracy conservativetribune.com");

        TextMessage reply = fakeNewsController.handleTextMessageEvent(event);

        assertEquals("conservativetribune.com is a conspiracy news website", reply.getText());
    }

    @Test
    void testHandleTextMessageEventFalseInput() {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("Wawiyu");

        TextMessage reply = fakeNewsController.handleTextMessageEvent(event);
        assertNotNull(reply.getText());
    }

    @Test
    void testHandleTextMessageEventAddFilter() {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/add_filter ExperimentalVaccines.org conspiracy");

        TextMessage reply = fakeNewsController.handleTextMessageEvent(event);

        assertEquals("conspiracy is already present", reply.getText());

    }

    @Test
    void testHandleDefaultMessage() {
        Event event = mock(Event.class);

        fakeNewsController.handleDefaultMessage(event);

        verify(event, atLeastOnce()).getSource();
        verify(event, atLeastOnce()).getTimestamp();
    }
}