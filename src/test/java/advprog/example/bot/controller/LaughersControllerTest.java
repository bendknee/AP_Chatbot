package advprog.example.bot.controller;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
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

    private LaughersController laughersController;

    @Mock
    private LaughersManager laughersManager;

    @BeforeEach
    void setUp() {
        laughersController = new LaughersController(laughersManager);
    }

    @Test
    void testContextLoads() {
        assertNotNull(laughersController);
    }

    @Test
    void testHandleTextMessageEventTopLaughers() {
        doReturn("1. Endrawan (100%)").when(laughersManager).getTop5Laughers("R1");

        MessageEvent<TextMessageContent> event =
            EventTestUtil.createDummyTextMessage("/toplaughers");

        TextMessage reply = laughersController.handleTextMessageEvent(event);

        assertEquals("1. Endrawan (100%)", reply.getText());
    }

    @Test
    void testHandleTextMessageEventOtherMessage() {
        MessageEvent<TextMessageContent> event =
            EventTestUtil.createDummyTextMessage("halo");

        laughersController.handleTextMessageEvent(event);

        verify(laughersManager, atLeastOnce()).processMessage(any(), any(), any());
    }
}
