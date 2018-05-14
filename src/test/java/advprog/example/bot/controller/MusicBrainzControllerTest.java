package advprog.example.bot.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import advprog.example.bot.EventTestUtil;
import com.linecorp.bot.model.event.Event;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest(properties = "line.bot.handler.enabled=false")
@ExtendWith(SpringExtension.class)
public class MusicBrainzControllerTest {

    static {
        System.setProperty("line.bot.channelSecret", "SECRET");
        System.setProperty("line.bot.channelToken", "TOKEN");
    }

    @Autowired
    private MusicBrainzController musicBrainzController;

    @Test
    void testContextLoads() {
        assertNotNull(musicBrainzController);
    }

    @Test
    void testHandleTextMessageEvent() {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/echo Lorem Ipsum");

        TextMessage reply = musicBrainzController.handleTextMessageEvent(event);

        assertNull(reply);
    }

    @Test
    void testHandleDefaultMessage() {
        Event event = mock(Event.class);

        event.getSource();
        event.getTimestamp();

        musicBrainzController.handleDefaultMessage(event);

        verify(event, Mockito.atLeastOnce()).getSource();
        verify(event, Mockito.atLeastOnce()).getTimestamp();
    }
}
