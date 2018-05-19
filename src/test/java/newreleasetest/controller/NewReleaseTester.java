package newreleasetest.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import advprog.example.bot.EventTestUtil;
import botnewrelease.NewReleaseApp;
import botnewrelease.controller.NewReleaseController;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;

import com.ritaja.xchangerate.api.CurrencyNotSupportedException;
import com.ritaja.xchangerate.endpoint.EndpointException;
import com.ritaja.xchangerate.service.ServiceException;
import com.ritaja.xchangerate.storage.StorageException;

import java.io.IOException;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class NewReleaseTester {

    static {
        System.setProperty("line.bot.channelSecret", "6d36e276040"
                + "357906dc55e6910be12fc");
        System.setProperty("line.bot.channelToken", "QpMXcgUuMTdYJ1SkPA6JOH5NmAC"
                + "87qulDl/RM5haiibS7pBq69Za1Z7RkWzLERjTTOz0c4FTK2eiMKbabg/0"
                + "1pwRv6HSxWOcbfmfZEzqIh+7V7Tq7bOFXk/MA/cQPBp0C5sSY"
                + "fjnFKyHnL9B155ogwdB04t89/1O/w1cDnyilFU=");
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

        TextMessage reply = newReleaseController.handleTextMessageEvent(event);

        assertEquals("Sorry your input is not valid "
                + "the format should be /vgmdb OST this month", reply.getText());
    }

    @Test
    void testillegalArgument() throws IOException, JSONException,
            CurrencyNotSupportedException,
            ServiceException, EndpointException, StorageException {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/uiweiwuebwbcw"
                        + "cuwehjwehcuewvubevwv");

        TextMessage reply = newReleaseController.handleTextMessageEvent(event);

        assertEquals("Sorry your input is not valid "
                + "the format should be /vgmdb OST this month", reply.getText());
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
