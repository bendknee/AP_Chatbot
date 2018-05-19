package advprog.example.bot.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.junit.jupiter.api.Assertions.assertTrue;
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
public class BotControllerTest {

    static {
        System.setProperty("line.bot.channelSecret", "SECRET");
        System.setProperty("line.bot.channelToken", "TOKEN");
    }

    @Autowired
    private BotController echoController;


    @Test
    void testContextLoads() {
        assertNotNull(echoController);
    }

    @Test
    void testHandleTextMessageEvent() {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/echo lookup_anime");

        TextMessage reply = echoController.handleTextMessageEvent(event);

        assertEquals("", reply.getText());
    }

    @Test
    void testHandleTextMessageEventFilm() {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/film Romance");

        TextMessage reply = echoController.handleTextMessageEvent(event);

        assertEquals("Romance", reply.getText());
    }
    @Test
    void testHandleTextMessageEventFilmSeason() {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/film/season winter");

        TextMessage reply = echoController.handleTextMessageEvent(event);

        assertEquals("winter", reply.getText());
    }

    @Test
    void testHandleYearMessageEvent() {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/film/year 2012");

        TextMessage reply = echoController.handleTextMessageEvent(event);

        assertTrue(reply.getText().contains("High School"));
    }

    @Test
    void testHandleDefaultMessage() {
        Event event = mock(Event.class);

        echoController.handleDefaultMessage(event);

        verify(event, atLeastOnce()).getSource();
        verify(event, atLeastOnce()).getTimestamp();
    }
}