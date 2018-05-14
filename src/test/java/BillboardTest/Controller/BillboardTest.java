package billboardtest.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import advprog.example.bot.EventTestUtil;

import billboardpackage.BillboardApp;
import billboardpackage.controller.BillboardController;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;

import java.io.IOException;

import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest(properties = "line.bot.handler.enabled=false")
@ExtendWith(SpringExtension.class)
public class BillboardTest {

    private BillboardController billboardController = new BillboardController();

    static {
        System.setProperty("line.bot.channelSecret", "976d1b06307ba2"
                + "536326e9a69aec048f");
        System.setProperty("line.bot.channelToken", "ziqv6nFQ2dpydj2FvuMdI"
                + "zPNhVpcppcg86ZNUSXO/qHfir4+FvT1w9/fypH/pKHisx7peMAk"
                + "CuZ1AB7SoqNf+nlMPmpEbTlLz4/tHcWkSu6VGZY7ehHlaCVhAqr"
                + "RM9RahU7gvEb1Eju00cvvuPp4vgd"
                + "B04t89/1O/w1cDnyilFU=");
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

    @Test
    public void applicationContextTest() {
        BillboardApp.main(new String[]{});
    }

    @Test
    public void testillegalArgument() throws IOException {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("wuigcedcvwdcvw"
                        + "dscbdscsdcdcubsdc");

        TextMessage reply = billboardController.handleTextMessageEvent(event);
        assertEquals("Sorry, Artist wuigcedcvwdcvw"
                + "dscbdscsdcdcubsdc is not available", reply.getText());
    }

    @Test
    public void testsuksesArgument() throws IOException {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/billboard "
                        + "bill200 janelle monae");

        TextMessage reply = billboardController.handleTextMessageEvent(event);
        assertEquals("\n" + "Janelle Monae" + "\n"
                + "Dirty Computer"
                + "\n" + "Position : " + 6 + "\n", reply.getText());
    }
}
