package newreleasetest.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import advprog.example.bot.EventTestUtil;
import botnewrelease.NewReleaseApp;
import botnewrelease.controller.NewReleaseController;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;

import com.ritaja.xchangerate.api.CurrencyNotSupportedException;
import com.ritaja.xchangerate.endpoint.EndpointException;
import com.ritaja.xchangerate.service.ServiceException;
import com.ritaja.xchangerate.storage.StorageException;

import java.io.IOException;
import java.util.List;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest(properties = "line.bot.handler.eaabled=false", classes = NewReleaseApp.class)
@ExtendWith(SpringExtension.class)
public class NewReleaseTester {

    static {
        System.setProperty("line.bot.channelSecret", "6aaea5f3be3aff04"
                + "b50ff183727493a3");
        System.setProperty("line.bot.channelToken", "zoSKZdAdyRLBZ5TMpmVf5VR/j0AVVaax1a"
                + "HLnXoAvnwvB1zzVWmcHdHIQ/Hm1wmg55KuC1EOEqggMIOcuo2DNP8JL1tw"
                + "3wh7kIl8R2gAOiKLTdVb7oLUHRDWSrKZo51y8EUrV+nDn1aF0ehWVM"
                + "jw0AdB04t89/1O/w1cDnyilFU=");
    }

    private NewReleaseController newReleaseController = new NewReleaseController();

    @Test
    void testContextLoads() {
        assertNotNull(newReleaseController);
    }

    @Test
    void testHandleTextMessageEvent() throws IOException, JSONException,
            CurrencyNotSupportedException,
            ServiceException, EndpointException, StorageException {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/VJBEVEVVFN");

        List<TextMessage> reply = newReleaseController.handleTextMessageEvent(event);

        assertEquals("The format should be /vgmdb "
                + "OST this month", reply.get(0).getText());
    }

    @Test
    void testillegalArgument() throws IOException, JSONException,
            CurrencyNotSupportedException,
            ServiceException, EndpointException, StorageException {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/uiweiwuebwbcw"
                        + "cuwehjwehcuewvubevwv");

        List<TextMessage> reply = newReleaseController.handleTextMessageEvent(event);

        assertEquals("Sorry, your input is not valid "
                + "it should be /vgmdb OST this month", reply.get(0).getText());
    }

    @Test
    void testHandleDefaultMessage() {
        Event event = mock(Event.class);

        newReleaseController.handleDefaultMessage(event);

        verify(event, atLeastOnce()).getSource();
        verify(event, atLeastOnce()).getTimestamp();
    }

    @Test
    public void applicationContextTest() {
        NewReleaseApp.main(new String[]{});
    }
}
