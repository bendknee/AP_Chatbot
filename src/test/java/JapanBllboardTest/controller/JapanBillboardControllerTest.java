package JapanBllboardTest.controller;

import advprog.example.bot.EventTestUtil;
import JapanBillboard.JapanBotBillBoard;
import JapanBillboard.controller.JapanBillboardController;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest(properties = "line.bot.handler.enabled=false")
@ExtendWith(SpringExtension.class)
public class JapanBillboardControllerTest {

    JapanBillboardController japanBillboardController = new JapanBillboardController();

    static {
        System.setProperty("line.bot.channelSecret", "c30b82a49293"
                + "b7c16a9bf6488b7d3e63");
        System.setProperty("line.bot.channelToken", "g3S2UFypbYUxSPFjlgr0TA96yKG+R"
                + "ILbXbowKiis43NmW/285W84e7zAVPuW+L"
                + "8ZZuiPyakJNVmzouENCttynmsFPVkQZEM5zDUGbjdkCW0WCK8ISqtlF9vQ3"
                + "frGBsSbcR401NTPOiid0VFND71YhQdB04"
                + "t89/1O/w1cDnyilFU=");
    }

    @Test
    public void testContextLoads() {
        assertNotNull(japanBillboardController);
    }

    @Test
    public void testHandleTextMessageEvent() throws IOException {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/billboard japan100 Yeah");

        TextMessage reply = japanBillboardController.handleTextMessageEvent(event);

        assertEquals(reply.getText(), "Sorry, Artist Yeah is not in the chart");
    }

    @Test
    public void testErrorMessage() throws IOException {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/BLABLABLA");

        TextMessage reply = japanBillboardController.handleTextMessageEvent(event);
        assertEquals("Sorry your input is not valid", reply.getText());
    }

    @Test
    public void testHandleDefaultMessage() {
        Event event = mock(Event.class);

        japanBillboardController.handleDefaultMessage(event);

        verify(event, atLeastOnce()).getSource();
        verify(event, atLeastOnce()).getTimestamp();
    }

    @Test
    public void applicationContextTest() {
        JapanBotBillBoard.main(new String[]{});
    }
}