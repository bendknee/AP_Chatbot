package advprog.example.bot.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;

import advprog.example.bot.EventTestUtil;
import junit.framework.TestCase;

@SpringBootTest(properties = "line.bot.handler.enabled=false")
@ExtendWith(SpringExtension.class)
public class OriconBookRankingControllerTest extends TestCase {

    static {
        System.setProperty("line.bot.channelSecret", "SECRET");
        System.setProperty("line.bot.channelToken", "TOKEN");
    }

    @Autowired
    public OriconBookRankingController oriconBookRankingController = new OriconBookRankingController();

    @Test
    public void testContextLoads() {
        assertNotNull(oriconBookRankingController);
    }

    @Test
    public void testHandleTextMessageEventValidInput() throws Exception {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/oricon book weekly 2018-05-14");

        TextMessage reply = oriconBookRankingController.handleTextMessageEvent(event);
        String[] lines = reply.getText().split("\n");
        assertEquals(10, lines.length);
    }
      
    
    @Test
    public void testHandleTextMessageEventInvalidInputWithDate() throws Exception {
    	MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/oricon book weekly 2018-05-13");
        TextMessage reply = oriconBookRankingController.handleTextMessageEvent(event);
        assertEquals(reply.getText(),
                "Invalid Parameter, either your date is not available "
                + "or you have given a wrong input");
    }
    
    @Test
    public void testHandleTextMessageEventInvalidParameter1() throws Exception {
    	MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/oricon book weekly 2018-48-12-10");
        TextMessage reply = oriconBookRankingController.handleTextMessageEvent(event);
        assertEquals(reply.getText(),
                "Invalid Parameter, either your date is not available "
                + "or you have given a wrong input");
    }
    
    @Test
    public void testHandleTextMessageEventInvalidParameter2() throws Exception {
    	MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/oricon book weekly hehe");
        TextMessage reply = oriconBookRankingController.handleTextMessageEvent(event);
        assertEquals(reply.getText(),
                "Invalid Parameter, either your date is not available "
                + "or you have given a wrong input");
    }
    
    @Test
    public void testHandleTextMessageEventWithoutParameter() throws Exception {
    	MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/oricon book weekly");
        TextMessage reply = oriconBookRankingController.handleTextMessageEvent(event);
        assertEquals(reply.getText(),
                "/oricon book weekly function needs a date as a parameter");
    }
    
    @Test
    public void testHandleTextMessageEventUnknownMethod() throws Exception {
    	MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/random");
        TextMessage reply = oriconBookRankingController.handleTextMessageEvent(event);
        assertEquals(reply.getText(),
                "Command doesn't exist, try: \n" + "/oricon books weekly <date YYYY-MM-DD>");
    }     
    
    public void testHandleTextMessageEventNonMethod() throws Exception {
    	MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("random");
        TextMessage reply = oriconBookRankingController.handleTextMessageEvent(event);
        assertEquals(reply , null);
    }
    
    @Test
    public void testHandleDefaultMessage() {
        Event event = mock(Event.class);

        oriconBookRankingController.handleDefaultMessage(event);

        verify(event, atLeastOnce()).getSource();
        verify(event, atLeastOnce()).getTimestamp();
    }
}