package advprog.example.bot.controller;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import advprog.example.bot.EventTestUtil;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.source.GroupSource;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;

import java.time.Instant;
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

        Message reply = sacredTextController.handleTextMessageEvent(event);

        assertEquals("To him the richest of the rich, the Lord of treasures excellent,\n"
            + "Indra, with Soma juice outpoured.", ((TextMessage) reply).getText());
    }
    
    @Test
    public void testHandleTextMessageIncorrectParameterizedEventTooManyParameter()
        throws Exception {
        MessageEvent<TextMessageContent> event =
            EventTestUtil.createDummyTextMessage("/sacred_text 5:2 extra");

        Message reply = sacredTextController.handleTextMessageEvent(event);

        assertEquals("Parameter should just be <Chapter>:<Verse>", ((TextMessage) reply).getText());
    }
    
    @Test
    public void testHandleTextMessageIncorrectParameterizedFormatEvent1() throws Exception {
        MessageEvent<TextMessageContent> event =
            EventTestUtil.createDummyTextMessage("/sacred_text 5");

        Message reply = sacredTextController.handleTextMessageEvent(event);

        assertEquals("Parameter should just be <Chapter>:<Verse>", ((TextMessage) reply).getText());
    }
    
    @Test
    public void testHandleTextMessageIncorrectParameterizedFormatEvent2() throws Exception {
        MessageEvent<TextMessageContent> event =
            EventTestUtil.createDummyTextMessage("/sacred_text 5:2:1");

        Message reply = sacredTextController.handleTextMessageEvent(event);

        assertEquals("Parameter should just be <Chapter>:<Verse>", ((TextMessage) reply).getText());
    }
    
    @Test
    public void testHandleTextMessageIncorrectChapter() throws Exception {
        MessageEvent<TextMessageContent> event =
            EventTestUtil.createDummyTextMessage("/sacred_text notInt:2");

        Message reply = sacredTextController.handleTextMessageEvent(event);

        assertEquals("Chapter and Verse must both be an integer", ((TextMessage) reply).getText());
    }
    
    @Test
    public void testHandleTextMessageIncorrectVerse() throws Exception {
        MessageEvent<TextMessageContent> event =
            EventTestUtil.createDummyTextMessage("/sacred_text 5:notInt");

        Message reply = sacredTextController.handleTextMessageEvent(event);

        assertEquals("Chapter and Verse must both be an integer", ((TextMessage) reply).getText());
    }
    
    @Test
    public void testHandleTextMessageInvalidChapter() throws Exception {
        MessageEvent<TextMessageContent> event =
            EventTestUtil.createDummyTextMessage("/sacred_text 200:1");

        Message reply = sacredTextController.handleTextMessageEvent(event);

        assertEquals("Chapter not available\n Available Chapters: 1-191",
            ((TextMessage) reply).getText());
    }
    
    @Test
    public void testHandleTextMessageInvalidVerse() throws Exception {
        MessageEvent<TextMessageContent> event =
            EventTestUtil.createDummyTextMessage("/sacred_text 103:0");

        Message reply = sacredTextController.handleTextMessageEvent(event);

        assertEquals("Invalid Verse Number\nVerse Range(inclusive): 1-8",
            ((TextMessage) reply).getText());
    }
    
    @Test
    public void testHandleTextMessageUnknownFunctionEvent() throws Exception {
        MessageEvent<TextMessageContent> event =
            EventTestUtil.createDummyTextMessage("/random");

        Message reply = sacredTextController.handleTextMessageEvent(event);

        assertEquals("Command doesn't exist\n"
            + "Try: /sacred_text OR /sacred_text <Chapter>:<Verse>",
            ((TextMessage) reply).getText());
    }
    
    @Test
    public void testHandleTextMessageInGroupAnsweredCorrectlyFirstAttempt() throws Exception {
        MessageEvent<TextMessageContent> event = new MessageEvent<TextMessageContent>(
            "replyToken", new GroupSource("groupId", "userId"),
            new TextMessageContent("id", "randomVerse"),
            Instant.now());
        Message reply = sacredTextController.handleTextMessageEvent(event);
        assertTrue(((TextMessage) reply).getText().contains(
            "Guess the chapter from Book 1 of The Rig Veda!"));
        int answer = sacredTextController.getRandomChapter();
        event = new MessageEvent<TextMessageContent>(
            "replyToken", new GroupSource("groupId", "userId"),
            new TextMessageContent("id", "" + answer),
            Instant.now());
        reply = sacredTextController.handleTextMessageEvent(event);
        assertEquals("You are correct", ((TextMessage) reply).getText());

    }

    public void testHandleTextMessageInGroupAnsweredCorrectlySecondAttempt() throws Exception {
        MessageEvent<TextMessageContent> event = new MessageEvent<TextMessageContent>(
            "replyToken", new GroupSource("groupId", "userId"),
            new TextMessageContent("id", "randomVerse"),
            Instant.now());
        Message reply = sacredTextController.handleTextMessageEvent(event);
        assertTrue(((TextMessage) reply).getText().contains(
            "Guess the chapter from Book 1 of The Rig Veda!"));
        event = new MessageEvent<TextMessageContent>(
            "replyToken", new GroupSource("groupId", "userId"),
            new TextMessageContent("id", "0"),
            Instant.now());
        reply = sacredTextController.handleTextMessageEvent(event);
        assertEquals("Incorrect\nTry again, chances remaining: 4", ((TextMessage) reply).getText());
        int answer = sacredTextController.getRandomChapter();
        event = new MessageEvent<TextMessageContent>(
            "replyToken", new GroupSource("groupId", "userId"),
            new TextMessageContent("id", "" + answer),
            Instant.now());
        reply = sacredTextController.handleTextMessageEvent(event);
        assertEquals("You are correct", ((TextMessage) reply).getText());

    }

    public void testHandleTextMessageInGroupAnsweredIncorrectly() throws Exception {
        MessageEvent<TextMessageContent> event = new MessageEvent<TextMessageContent>(
            "replyToken", new GroupSource("groupId", "userId"),
            new TextMessageContent("id", "randomVerse"),
            Instant.now());
        Message reply = sacredTextController.handleTextMessageEvent(event);
        assertTrue(((TextMessage) reply).getText().contains(
            "Guess the chapter from Book 1 of The Rig Veda!"));
        event = new MessageEvent<TextMessageContent>(
            "replyToken", new GroupSource("groupId", "userId"),
            new TextMessageContent("id", "0"),
            Instant.now());
        reply = sacredTextController.handleTextMessageEvent(event);
        assertEquals("Incorrect\nTry again, chances remaining: 4", ((TextMessage) reply).getText());
        event = new MessageEvent<TextMessageContent>(
            "replyToken", new GroupSource("groupId", "userId"),
            new TextMessageContent("id", "0"),
            Instant.now());
        reply = sacredTextController.handleTextMessageEvent(event);
        assertEquals("Incorrect\nTry again, chances remaining: 3", ((TextMessage) reply).getText());
        event = new MessageEvent<TextMessageContent>(
            "replyToken", new GroupSource("groupId", "userId"),
            new TextMessageContent("id", "0"),
            Instant.now());
        reply = sacredTextController.handleTextMessageEvent(event);
        assertEquals("Incorrect\nTry again, chances remaining: 2", ((TextMessage) reply).getText());
        event = new MessageEvent<TextMessageContent>(
            "replyToken", new GroupSource("groupId", "userId"),
            new TextMessageContent("id", "0"),
            Instant.now());
        reply = sacredTextController.handleTextMessageEvent(event);
        assertEquals("Incorrect\nTry again, chances remaining: 1", ((TextMessage) reply).getText());
        int answer = sacredTextController.getRandomChapter();
        event = new MessageEvent<TextMessageContent>(
            "replyToken", new GroupSource("groupId", "userId"),
            new TextMessageContent("id", "0"),
            Instant.now());
        reply = sacredTextController.handleTextMessageEvent(event);
        assertEquals("Incorrect\nCorrect answer is " + answer, ((TextMessage) reply).getText());

    }

    public void testHandleTextMessageInGroupMultipleGameAttempt() throws Exception {
        MessageEvent<TextMessageContent> event = new MessageEvent<TextMessageContent>(
            "replyToken", new GroupSource("groupId", "userId"),
            new TextMessageContent("id", "randomVerse"),
            Instant.now());
        Message reply = sacredTextController.handleTextMessageEvent(event);
        assertTrue(((TextMessage) reply).getText().contains(
            "Guess the chapter from Book 1 of The Rig Veda!"));
        int answer = sacredTextController.getRandomChapter();
        event = new MessageEvent<TextMessageContent>(
            "replyToken", new GroupSource("groupId", "userId"),
            new TextMessageContent("id", "randomVerse"),
            Instant.now());
        reply = sacredTextController.handleTextMessageEvent(event);
        assertEquals("Another game is still ongoing\n"
            + "Complete the game before starting a new one", ((TextMessage) reply).getText());

    }

    @Test
    public void testHandleTextMessageInGroupAttempToAnswerNonInteger() throws Exception {
        MessageEvent<TextMessageContent> event = new MessageEvent<TextMessageContent>(
            "replyToken", new GroupSource("groupId", "userId"),
            new TextMessageContent("id", "randomVerse"),
            Instant.now());
        Message reply = sacredTextController.handleTextMessageEvent(event);
        assertTrue(((TextMessage) reply).getText().contains(
            "Guess the chapter from Book 1 of The Rig Veda!"));
        event = new MessageEvent<TextMessageContent>(
            "replyToken", new GroupSource("groupId", "userId"),
            new TextMessageContent("id", "answer"),
            Instant.now());
        reply = sacredTextController.handleTextMessageEvent(event);
        assertEquals(null, reply);

    }



    @Test
    public void testHandleDefaultMessage() {
        Event event = mock(Event.class);

        sacredTextController.handleDefaultMessage(event);

        verify(event, atLeastOnce()).getSource();
        verify(event, atLeastOnce()).getTimestamp();
    }
}