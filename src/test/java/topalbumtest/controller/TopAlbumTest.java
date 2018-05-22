package topalbumtest.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import advprog.example.bot.EventTestUtil;

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

import topalbumpac.TopAlbumPac;
import topalbumpac.controller.TopAlbumsControl;

@SpringBootTest(properties = "line.bot.handler.enabled=false", classes = TopAlbumPac.class)
@ExtendWith(SpringExtension.class)
public class TopAlbumTest {

    static {
        System.setProperty("line.bot.channelSecret", "9e514c1bbfd82d"
                + "65c9c62738e335ad0c");
        System.setProperty("line.bot.channelToken", "ZBfd4J0OQJsmM96kLQv8lJ"
                + "Z6HROSPl082xGLEE1Mhmc5gofdxygtkYDkErflbC4hi89cYnBU"
                + "L139T8yemI0yjX9J5LeWsWf1LDJviTYReTkbl15Pu1Kje9"
                + "wBce9VP1hEtIoARacjxCQJu9hRj5iy3wd"
                + "B04t89/1O/w1cDnyilFU=");
    }

    private TopAlbumsControl control = new TopAlbumsControl();

    @Test
    void testContextLoads() {
        assertNotNull(control);
    }

    @Test
    void testHandleTextMessageEvent() throws IOException, JSONException,
            CurrencyNotSupportedException,
            ServiceException, EndpointException, StorageException {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/VJBEVEVVFN");

        TextMessage reply = control.handleTextMessageEvent(event);

        assertEquals("Sorry your input is not valid "
                + "the format should be /vgmdb most_popular", reply.getText());
    }

    @Test
    void testillegalArgument() throws IOException, JSONException,
            CurrencyNotSupportedException,
            ServiceException, EndpointException, StorageException {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/wvevuyebverbver"
                        + "vervcerverbeverv");

        TextMessage reply = control.handleTextMessageEvent(event);

        assertEquals("Sorry your input is not valid "
                + "the format should be /vgmdb most_popular", reply.getText());
    }

    @Test
    void testSuksesArgument() throws IOException, JSONException,
            CurrencyNotSupportedException, ServiceException,
            EndpointException, StorageException {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/vgmdb most_popular");

        TextMessage reply = control.handleTextMessageEvent(event);

        assertTrue(reply.getText().contains("1 - CHRONO CROSS ORIGINAL "
                + "SOUNDTRACK - 4.72 (429700 IDR)"));
    }

    @Test
    void testHandleDefaultMessage() {
        Event event = mock(Event.class);

        control.handleDefaultMessage(event);

        verify(event, atLeastOnce()).getSource();
        verify(event, atLeastOnce()).getTimestamp();
    }

    @Test
    public void applicationContextTest() {
        TopAlbumPac.main(new String[]{});
    }
}
