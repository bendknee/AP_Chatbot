package BillboardTest.Controller;

import advprog.example.bot.EventTestUtil;
import BillboardPackage.BillboardApp;
import BillboardPackage.controller.BillboardController;
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

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest(properties = "line.bot.handler.enabled=false")
@ExtendWith(SpringExtension.class)
public class BillboardTest {

    private BillboardController billboardController = new BillboardController();

    static {
        System.setProperty("line.bot.channelSecret", "3f43b356681d321342cbc7bf2464207c");
        System.setProperty("line.bot.channelToken", "1zaKuQmgqKzBRxRxc4m5dSWebWQmxKhPuc39t2zQcrkR8i0/EbEL/RKdKDJjmlDRAT5Byf7nIMNPMnVNphJSn4TaJSShtv1cPd7PcME3EL6qitcLV8aeMsrL18HcZ2Q9+PHNqTDESpDY4El2z9cZVQdB04t89/1O/w1cDnyilFU=");
    }

    @Test
    public void testContextLoads() {
        assertNotNull(billboardController);
    }

    @Test
    public void testHandleTextMessageEvent() throws IOException {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/billboard bill200 Katy");

        TextMessage reply = billboardController.handleTextMessageEvent(event);

        assertEquals(reply.getText(), "Sorry, "
                + "Artist Katy is not in the chart");
    }

    @Test
    public void testErrorMessage() throws IOException {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/BLABLABLA");

        TextMessage reply = billboardController.handleTextMessageEvent(event);
        assertEquals("Sorry input not valid the f"
                + "ormat should be /billboard bill200 Artist"
                + "Name", reply.getText());
    }

    @Test
    public void testHandleDefaultMessage() {
        Event event = mock(Event.class);

        billboardController.handleDefaultMessage(event);

        verify(event, atLeastOnce()).getSource();
        verify(event, atLeastOnce()).getTimestamp();
    }

/*    @Test
    public void applicationContextTest() {
        BillboardApp.main(new String[]{});
    }*/
}
