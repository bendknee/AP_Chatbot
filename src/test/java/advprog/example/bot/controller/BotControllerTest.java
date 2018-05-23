package advprog.example.bot.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import advprog.example.bot.EventTestUtil;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.PostbackEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest(properties = "line.bot.handler.enabled=false")
@ExtendWith(SpringExtension.class)
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class})
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
    void testHandleEchoCommand() throws Exception {
        String command = "/echo Hello world!";
        MessageEvent<TextMessageContent> message = EventTestUtil.createDummyTextMessage(command);

        TextMessage reply = (TextMessage) botController.handleTextMessageEvent(message);
        String replyText = reply.getText();

        assertEquals("Hello world!", replyText);
    }

    @Test
    void testHandleAddQuestionCommand() throws Exception {
        String command = "/add_question";

        MessageEvent<TextMessageContent> message = EventTestUtil.createDummyTextMessage(command);

        TextMessage reply = (TextMessage) botController.handleTextMessageEvent(message);
        String replyText = reply.getText();

        assertEquals("Enter your question.", replyText);

        reply = (TextMessage) botController.handleTextMessageEvent(message);
        replyText = reply.getText();

        assertEquals("Enter 4 answers.", replyText);

        botController.handleTextMessageEvent(message);
        botController.handleTextMessageEvent(message);
        botController.handleTextMessageEvent(message);

        TemplateMessage carousel = (TemplateMessage) botController.handleTextMessageEvent(message);
        replyText = carousel.getAltText();

        assertEquals("Select correct answer", replyText);

        PostbackEvent postbackEvent = EventTestUtil.createDummyPostbackEvent("0");
        reply = (TextMessage) botController.handlePostbackEvent(postbackEvent);

        assertEquals("pertanyaan:\n/add_question\n\njawaban:\n/add_question\n"
                        + "/add_question\n/add_question\n/add_question\n\njawaban benar:\n"
                        + "/add_question", reply.getText());
    }

    @Test
    void testDefaultHandler() {
        BotController botControllerSpy = spy(botController);
        Event eventMock = mock(Event.class);

        botControllerSpy.handleDefaultMessage(eventMock);
        verify(botControllerSpy, atLeastOnce()).handleDefaultMessage(eventMock);
    }
}
