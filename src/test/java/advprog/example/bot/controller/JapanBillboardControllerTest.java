package advprog.billboard.japan.controller;

import advprog.billboard.japan.EventTestUtil; 
import advprog.billboard.japan.LineBillboardJapanApplication;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest(properties = "line.bot.handler.enabled=false")
@ExtendWith(SpringExtension.class)
@RunWith( SpringJUnit4ClassRunner.class )
public class JapanBillboardControllerTest {
    @Autowired
    JapanBillboardController japanBillboardController;

    static {
        System.setProperty("line.bot.channelSecret", "SECRET");
        System.setProperty("line.bot.channelToken", "TOKEN");
    }

    @Test
    public void testContextLoads() {
        assertNotNull(japanBillboardController);
    }

    @Test
    public void testHandleTextMessageEvent() {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/billboard japan100");

        TextMessage reply = japanBillboardController.handleTextMessageEvent(event);

        assertEquals(reply.getText(), "(1) Me - Always Me");
    }

    @Test
    public void testErrorMessage() {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/hahaha");

        TextMessage reply = japanBillboardController.handleTextMessageEvent(event);
        assertEquals("Command not found for /hahaha", reply.getText());
    }

    @Test
    public void testHandleDefaultMessage() {
        Event event = mock(Event.class);

        japanBillboardController.handleDefaultMessage(event);

        verify(event, atLeastOnce()).getSource();
        verify(event, atLeastOnce()).getTimestamp();
    }

    @Test
    public void applicationContextLoaded() {
    }

    @Test
    public void applicationContextTest() {
        LineBillboardJapanApplication.main(new String[] {});
    }
}