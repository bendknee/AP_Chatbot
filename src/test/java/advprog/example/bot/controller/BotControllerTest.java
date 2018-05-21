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
import com.linecorp.bot.model.message.ImageMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
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
    private BotController botController;

    @Test
    void testContextLoads() {
        assertNotNull(botController);
    }

    @Test
    void testHandleTextMessageEventEchoCommand() {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/echo Lorem Ipsum");

        Message reply = botController.handleTextMessageEvent(event);
        TextMessage replyText = (TextMessage)reply;

        assertEquals("Lorem Ipsum", replyText.getText());
    }

    @Test
    void testHandleAddWikiCommand() {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/add_wiki http://marvel.wikia.com/api.php");

        Message reply = botController.handleTextMessageEvent(event);
        TextMessage replyText = (TextMessage)reply;

        String text = replyText.getText();
        assertTrue(text.contains("Wiki added"));
    }

    @Test
    void testHandleDefaultMessage() {
        Event event = mock(Event.class);

        Message reply = botController.handleDefaultMessageEvent(event);
        TextMessage replyText = (TextMessage)reply;

        assertEquals("Invalid command", replyText.getText());
    }


}