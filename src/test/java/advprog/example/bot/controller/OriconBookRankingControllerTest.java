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
public class OriconBookRankingControllerTest {

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
                .thenReturn(new TextMessage("(1) Book Title A - Author A - 2017-01-01 - 12345
                (2) Book Title B - Author B - 2017-01-01 - 12345
                (3) Book Title C - Author C - 2017-01-01 - 12345
                (4) Book Title D - Author D - 2017-01-01 - 12345
                (5) Book Title E - Author E - 2017-01-01 - 12345
                (6) Book Title F - Author F - 2017-01-01 - 12345
                (7) Book Title G - Author G - 2017-01-01 - 12345
                (8) Book Title H - Author H - 2017-01-01 - 12345
                (9) Book Title I - Author I - 2017-01-01 - 12345
                (10) Book Title J - Author J - 2017-01-01 - 12345"));
    }

    @Test
    public void testContextLoads() {
        assertNotNull(languageDetectionControllerMock);
    }

    @Test
    public void testHandleTextMessageEvent() {
        TextMessage reply = languageDetectionControllerMock.handleTextMessageEvent(textMessageContentMock);

        assertEquals("(1) Book Title A - Author A - 2017-01-01 - 12345
                (2) Book Title B - Author B - 2017-01-01 - 12345
                (3) Book Title C - Author C - 2017-01-01 - 12345
                (4) Book Title D - Author D - 2017-01-01 - 12345
                (5) Book Title E - Author E - 2017-01-01 - 12345
                (6) Book Title F - Author F - 2017-01-01 - 12345
                (7) Book Title G - Author G - 2017-01-01 - 12345
                (8) Book Title H - Author H - 2017-01-01 - 12345
                (9) Book Title I - Author I - 2017-01-01 - 12345
                (10) Book Title J - Author J - 2017-01-01 - 12345", reply.getText());
    }

    @Test
    public void testHandleDefaultMessage() {
        eventMock.getSource();
        eventMock.getTimestamp();

        verify(eventMock, atLeastOnce()).getSource();
        verify(eventMock, atLeastOnce()).getTimestamp();
    }

}
