package advprog.example.bot.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
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
class BotControllerTest {

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
    void testHandleEchoCommand() {
        String command = "/echo Hello world!";
        MessageEvent<TextMessageContent> message = EventTestUtil.createDummyTextMessage(command);

        TextMessage reply = (TextMessage) botController.handleTextMessageEvent(message);
        String replyText = reply.getText();

        assertEquals("Hello world!", replyText);
    }

    @Test
    void testHandleDetectLangCommand() {
        String command = "/detect_lang Hello world!";
        MessageEvent<TextMessageContent> message = EventTestUtil.createDummyTextMessage(command);
        String errorMessage = "Something is wrong with our 3rd party server. "
                + "Please try again later.";

        TextMessage reply = (TextMessage) botController.handleTextMessageEvent(message);
        String replyText = reply.getText();

        assertNotEquals(errorMessage, replyText);
    }

    @Test
    void testDefaultHandler() {
        BotController botControllerSpy = spy(botController);
        Event eventMock = mock(Event.class);

        botControllerSpy.handleDefaultMessage(eventMock);
        verify(botControllerSpy, atLeastOnce()).handleDefaultMessage(eventMock);
    }
}
