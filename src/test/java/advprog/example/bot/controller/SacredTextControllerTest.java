package advprog.example.bot.controller;

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

import junit.framework.TestCase;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest(properties = "line.bot.handler.enabled=false")
@ExtendWith(SpringExtension.class)
public class SacredTextControllerTest extends TestCase {

    static {
        System.setProperty("line.bot.channelSecret", "SECRET");
        System.setProperty("line.bot.channelToken", "TOKEN");
    }

    @Autowired
    public SacredTextController sacredTextController = new SacredTextController();

    @Test
    public void testContextLoads() {
        assertNotNull(sacredTextController);
    }

    @Test
    public void testHandleTextMessageValidParameterizedEvent() throws Exception {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/sacred_text 5:2");

        TextMessage reply = sacredTextController.handleTextMessageEvent(event);

        assertEquals("To him the richest of the rich, the Lord of treasures excellent,\n"
            + "Indra, with Soma juice outpoured.", reply.getText());
    }
    
    @Test
    public void testHandleTextMessageIncorrectParameterizedEventTooManyParameter()
        throws Exception {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/sacred_text 5:2 extra");

        TextMessage reply = sacredTextController.handleTextMessageEvent(event);

        assertEquals("Parameter should just be <Chapter>:<Verse>", reply.getText());
    }
    
    @Test
    public void testHandleTextMessageIncorrectParameterizedFormatEvent1() throws Exception {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/sacred_text 5");

        TextMessage reply = sacredTextController.handleTextMessageEvent(event);

        assertEquals("Parameter should just be <Chapter>:<Verse>", reply.getText());
    }
    
    @Test
    public void testHandleTextMessageIncorrectParameterizedFormatEvent2() throws Exception {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/sacred_text 5:2:1");

        TextMessage reply = sacredTextController.handleTextMessageEvent(event);

        assertEquals("Parameter should just be <Chapter>:<Verse>", reply.getText());
    }
    
    @Test
    public void testHandleTextMessageIncorrectChapter() throws Exception {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/sacred_text notInt:2");

        TextMessage reply = sacredTextController.handleTextMessageEvent(event);

        assertEquals("Chapter and Verse must both be an integer", reply.getText());
    }
    
    @Test
    public void testHandleTextMessageIncorrectVerse() throws Exception {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/sacred_text 5:notInt");

        TextMessage reply = sacredTextController.handleTextMessageEvent(event);

        assertEquals("Chapter and Verse must both be an integer", reply.getText());
    }
    
    @Test
    public void testHandleTextMessageInvalidChapter() throws Exception {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/sacred_text 200:1");

        TextMessage reply = sacredTextController.handleTextMessageEvent(event);

        assertEquals("Chapter not available\n Available Chapters: 1-191", reply.getText());
    }
    
    @Test
    public void testHandleTextMessageInvalidVerse() throws Exception {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/sacred_text 103:0");

        TextMessage reply = sacredTextController.handleTextMessageEvent(event);

        assertEquals("Invalid Verse Number\nVerse Range(inclusive): 1-8", reply.getText());
    }
    
    @Test
    public void testHandleTextMessageUnknownFunctionEvent() throws Exception {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/random");

        TextMessage reply = sacredTextController.handleTextMessageEvent(event);

        assertEquals("Command doesn't exist\n"
                + "Try: /sacred_text OR /sacred_text <Chapter>:<Verse>", reply.getText());
    }
    
    

    @Test
    public void testHandleDefaultMessage() {
        Event event = mock(Event.class);

        sacredTextController.handleDefaultMessage(event);

        verify(event, atLeastOnce()).getSource();
        verify(event, atLeastOnce()).getTimestamp();
    }
}