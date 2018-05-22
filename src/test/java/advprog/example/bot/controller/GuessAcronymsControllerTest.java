package advprog.example.bot.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import advprog.example.bot.EventTestUtil;
import advprog.example.bot.manager.GuessAcronymsManager;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest(properties = "line.bot.handler.enabled=false")
@ExtendWith(SpringExtension.class)
public class GuessAcronymsControllerTest {

    static {
        System.setProperty("line.bot.channelSecret", "SECRET");
        System.setProperty("line.bot.channelToken", "TOKEN");
    }

    private GuessAcronymsController guessAcronymsController;

    @Mock
    private GuessAcronymsManager guessAcronymsManager;

    @BeforeEach
    void setUp() {
        guessAcronymsController = new GuessAcronymsController(guessAcronymsManager);
    }

    @Test
    void testContextLoads() {
        assertNotNull(guessAcronymsController);
    }

    @Test
    void testHandleTextMessageEventPrivateChat() {
        MessageEvent<TextMessageContent> event =
            EventTestUtil.createDummyTextMessagePrivateChat("Hello from private chat");

        guessAcronymsController.handleTextMessageEvent(event);

        verify(guessAcronymsManager, atLeastOnce())
            .handlePrivateChat("U1", "Hello from private chat", "replyToken");
        verify(guessAcronymsManager, never()).handleGroupChat(any(), any(), any(), any());
    }

    @Test
    void testHandleTextMessageEventGroupChat() {
        MessageEvent<TextMessageContent> event =
            EventTestUtil.createDummyTextMessageGroupChat("Hello from group chat");

        guessAcronymsController.handleTextMessageEvent(event);

        verify(guessAcronymsManager, atLeastOnce())
            .handleGroupChat("C1", "U1", "Hello from group chat", "replyToken");
        verify(guessAcronymsManager, never()).handlePrivateChat(any(), any(), any());
    }
}
