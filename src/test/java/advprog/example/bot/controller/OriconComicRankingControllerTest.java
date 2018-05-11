package advprog.example.bot.controller;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

@SpringBootTest(properties = "line.bot.handler.enabled=false")
@ExtendWith(SpringExtension.class)
public class OriconComicRankingControllerTest {

    static {
        System.setProperty("line.bot.channelSecret", "SECRET");
        System.setProperty("line.bot.channelToken", "TOKEN");
    }

    @Mock
    private static OriconComicRankingController oriconComicRankingControllerMock;

    @Mock
    private static MessageEvent<TextMessageContent> textMessageContentMock;

    @Mock
    private static Event eventMock;

    @Before
    @SuppressWarnings("unchecked")
    public void setUp() {
        oriconComicRankingControllerMock = mock(OriconComicRankingController.class);
        textMessageContentMock = (MessageEvent<TextMessageContent>) mock(MessageEvent.class);
        eventMock = mock(Event.class);

        when(languageDetectionControllerMock.handleTextMessageEvent(textMessageContentMock))
                .thenReturn(new TextMessage("(1) 進撃の巨人 - (画)諫山創\n"+
                "(2) 僕のヒーローアカデミア - (画)堀越耕平\n"+
                "(3) Book Title H - Author H\n"+
                "(4) Book Title G - Author G\n"+
                "(5) Book Title F - Author F\n"+
                "(6) Book Title E - Author E\n"+
                "(7) Book Title D - Author D\n"+
                "(8) Book Title C - Author C\n"+
                "(9) Book Title B - Author B\n"+
                "(10) Book Title A - Author A"));
    }

    @Test
    public void testContextLoads() {
        assertNotNull(languageDetectionControllerMock);
    }

    @Test
    public void testHandleTextMessageEvent() {
        TextMessage reply = languageDetectionControllerMock.handleTextMessageEvent(textMessageContentMock);

        assertEquals("(1) 進撃の巨人 - (画)諫山創\n"+
                "(2) 僕のヒーローアカデミア - (画)堀越耕平\n"+
                "(3) Book Title H - Author H\n"+
                "(4) Book Title G - Author G\n"+
                "(5) Book Title F - Author F\n"+
                "(6) Book Title E - Author E\n"+
                "(7) Book Title D - Author D\n"+
                "(8) Book Title C - Author C\n"+
                "(9) Book Title B - Author B\n"+
                "(10) Book Title A - Author A", reply.getText());
    }

    @Test
    public void testHandleDefaultMessage() {
        eventMock.getSource();
        eventMock.getTimestamp();

        verify(eventMock, atLeastOnce()).getSource();
        verify(eventMock, atLeastOnce()).getTimestamp();
    }

}
