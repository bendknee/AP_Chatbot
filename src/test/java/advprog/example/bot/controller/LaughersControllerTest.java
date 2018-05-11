package advprog.example.bot.controller;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import advprog.example.bot.EventTestUtil;
import advprog.example.bot.laughers.LaughersManager;

@SpringBootTest(properties = "line.bot.handler.enabled=false")
@ExtendWith(SpringExtension.class)
public class LaughersControllerTest {

    static {
        System.setProperty("line.bot.channelSecret", "SECRET");
        System.setProperty("line.bot.channelToken", "TOKEN");
    }

    @Autowired
    private LaughersController laughersController;

    @Mock
    private LaughersManager laughersManager;

    @Test
    void testContextLoads() {
        assertNotNull(laughersController);
    }

    @Test
    void testHandleTextMessageEvent() {
        doReturn("1. Endrawan (100%)").when(laughersManager).getTop5LaughersInGroup(1);

        MessageEvent<TextMessageContent> event =
            EventTestUtil.createDummyTextMessage("/toplaughers");

        TextMessage reply = laughersController.handleTextMessageEvent(event);

        assertEquals("1. Endrawan (100%)", reply.getText());
    }

    @Test
    void testHandleDefaultMessage() {
        Event event = mock(Event.class);

        laughersController.handleDefaultMessage(event);

        verify(event, atLeastOnce()).getSource();
        verify(event, atLeastOnce()).getTimestamp();
    }
}
