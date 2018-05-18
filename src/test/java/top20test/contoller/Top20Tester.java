package top20test.contoller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import advprog.example.bot.EventTestUtil;
import org.json.JSONException;
import top20albums.controller.top20Contoller;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.net.SocketTimeoutException;

@SpringBootTest(properties = "line.bot.handler.enabled=false")
@ExtendWith(SpringExtension.class)
public class Top20Tester {
    static {
        System.setProperty("line.bot.channelSecret", "6d36e276040"
                + "357906dc55e6910be12fc");
        System.setProperty("line.bot.channelToken", "QpMXcgUuMTdYJ1SkPA6JOH5NmAC"
                + "87qulDl/RM5haiibS7pBq69Za1Z7RkWzLERjTTOz0c4FTK2eiMKbabg/0"
                + "1pwRv6HSxWOcbfmfZEzqIh+7V7Tq7bOFXk/MA/cQPBp0C5sSY"
                + "fjnFKyHnL9B155ogwdB04t89/1O/w1cDnyilFU=");
    }

    private top20Contoller newReleaseController = new top20Contoller();

    @Test
    void testContextLoads() {
        assertNotNull(newReleaseController);
    }

    @Test
    void testHandleTextMessageEvent() throws IOException, JSONException, SocketTimeoutException {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/VJBEVEVVFN");

        TextMessage reply = newReleaseController.handleTextMessageEvent(event);

        assertEquals("Sorry your input is not valid "
                + "the format should be /vgmdb OST this month", reply.getText());
    }

    @Test
    void testillegalArgument() throws IOException, JSONException {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/wvevuyebverbver"
                        + "vervcerverbeverv");

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
}
