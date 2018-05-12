package TropicalBotTest.controller;

import advprog.example.bot.EventTestUtil;
import TropicalBot.controller.TropicalController;
import TropicalBot.BotBillboardTropical;
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
public class BotTropicalTest {

    TropicalController tropicalController = new TropicalController();

    static {
        System.setProperty("line.bot.channelSecret", "3f43b356681d32"
                + "1342cbc7bf2464207c");
        System.setProperty("line.bot.channelToken", "1zaKuQmgqKzBRxRxc4"
                + "m5dSWebWQmxKhPuc39t2zQcrkR8i0/EbEL/RKdK"
                + "DJjmlDRAT5Byf7nIMNPMnVNphJSn4TaJSShtv1cPd7P"
                + "cME3EL6qitcLV8aeMsrL18HcZ2Q9+PHNqTDESp"
                + "DY4El2z9cZVQdB04t89/1O/w1cDnyilFU=");
    }

    @Test
    public void testContextLoads() {
        assertNotNull(tropicalController);
    }

    @Test
    public void testHandleTextMessageEvent() throws IOException {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/billboard tropical Raymixx");

        TextMessage reply = tropicalController.handleTextMessageEvent(event);

        assertEquals(reply.getText(), "Sorry, Artist Raymixx is not in the chart");
    }

    @Test
    public void testErrorMessage() throws IOException {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/BLUBLABLU");

        TextMessage reply = tropicalController.handleTextMessageEvent(event);
        assertEquals("Sorry your input is not valid", reply.getText());
    }

    @Test
    public void testHandleDefaultMessage() {
        Event event = mock(Event.class);

        tropicalController.handleDefaultMessage(event);

        verify(event, atLeastOnce()).getSource();
        verify(event, atLeastOnce()).getTimestamp();
    }

/*    @Test
    public void applicationContextTest() {
        BotBillboardTropical.main(new String[]{});
    }*/
}
