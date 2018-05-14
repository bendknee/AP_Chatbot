package tropicalbottest.controller;

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

import java.io.IOException;

import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import tropicalbot.BotBillboardTropical;
import tropicalbot.controller.TropicalController;

@SpringBootTest(properties = "line.bot.handler.enabled=false")
@ExtendWith(SpringExtension.class)
public class BotTropicalTest {

    TropicalController tropicalController = new TropicalController();

    static {
        System.setProperty("line.bot.channelSecret", "4f5061aa776591"
                + "aad1bf35965ab5f25d");
        System.setProperty("line.bot.channelToken", "csN67un3gG09L80xWS5VjCcb0OM"
                + "3GqOpQjBd76HuJn1Go8Wwb4xQbPK9kRygi144i9dsvFGc6OUgFiHCdJfxcen"
                + "uByIV0ASTfk6xIxLwoC9fGE9+lqF/frcVm0AQUmukpJ1wR2kl"
                + "5+1b9t7Pdf6fdgdB04t89/1O/w1cDnyilFU=");
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
        assertEquals("Sorry your input is not valid "
                + "the format should be /billboard tropical "
                + "ArtistName", reply.getText());
    }

    @Test
    public void testHandleDefaultMessage() {
        Event event = mock(Event.class);

        tropicalController.handleDefaultMessage(event);

        verify(event, atLeastOnce()).getSource();
        verify(event, atLeastOnce()).getTimestamp();
    }

    @Test
    public void applicationContextTest() {
        BotBillboardTropical.main(new String[]{});
    }

    @Test
    public void testillegalArgument() throws IOException {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("wuigcedcvwdcvw"
                        + "dscbdscsdcdcubsdc");

        TextMessage reply = tropicalController.handleTextMessageEvent(event);
        assertEquals("Sorry, Artist wuigcedcvwdcvw"
                + "dscbdscsdcdcubsdc is not available", reply.getText());
    }
}
