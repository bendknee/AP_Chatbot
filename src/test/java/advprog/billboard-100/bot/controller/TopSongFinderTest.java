package advprog.billboard-100.bot.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import advprog.billboard-100.bot.BotBillboard100Test;

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
public class TopSongFinderTest {

    static {
        System.setProperty("line.bot.channelSecret", "SECRET");
        System.setProperty("line.bot.channelToken", "TOKEN");
    }

    @Autowired
    private TopSongFinder topSongFinder;

    @Test
    void testContextLoads() {
        assertNotNull(topSongFinder);
    }

    @Test
    void testHandleTextMessageEventSuccess() {
        MessageEvent<TextMessageContent> event = BotBillboard100Test
                .createDummyTextMessage("/billboard hot100");

        TextMessage reply = topSongFinder.handleTextMessageEvent(event);

        assertEquals("(1) Darude - Sandstorm\r\n" 
                + "(2) Simon & Garfunkel - Scarborough Fair\r\n"
                + "(3) Lazy Town - We Are Number One\r\n" + "...\r\n" 
                + "(10) Christopher Tin - Sogno di Volare\r\n"
                + "", reply.getText());
    }
    
    @Test
    void testHandleTextMessageEventError() {
        MessageEvent<TextMessageContent> event = BotBillboard100Test
                .createDummyTextMessage("/billboard hothot");

        TextMessage reply = topSongFinder.handleTextMessageEvent(event);

        assertEquals("error", reply.getText());
    }

    @Test
    void testHandleDefaultMessage() {
        Event event = mock(Event.class);

        topSongFinder.handleDefaultMessage(event);

        verify(event, atLeastOnce()).getSource();
        verify(event, atLeastOnce()).getTimestamp();
    }
}