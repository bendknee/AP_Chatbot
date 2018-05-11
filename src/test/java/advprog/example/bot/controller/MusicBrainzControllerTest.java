package advprog.example.bot.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.atLeastOnce;


import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest(properties = "line.bot.handler.enabled=false")
@ExtendWith(SpringExtension.class)
public class MusicBrainzControllerTest {

    static {
        System.setProperty("line.bot.channelSecret", "SECRET");
        System.setProperty("line.bot.channelToken", "TOKEN");
    }

    @Mock
    private static MusicBrainzController musicBrainzController;

    @Mock
    private static MessageEvent<TextMessageContent> textMessageContentMock;

    @Mock
    private static Event eventMock;

    @Before
    @SuppressWarnings("unchecked")
    public void setUp() {
        musicBrainzController = mock(MusicBrainzController.class);
        textMessageContentMock = (MessageEvent<TextMessageContent>) mock(MessageEvent.class);
        eventMock = mock(Event.class);

        when(musicBrainzController.handleTextMessageEvent(textMessageContentMock))
                .thenReturn(new TextMessage("Yo"));
    }

    @Test
    public void testContextLoads() {
        assertNotNull(musicBrainzController);
    }

    @Test
    void testHandleTextMessageEvent() {
        TextMessage reply = musicBrainzController.handleTextMessageEvent(textMessageContentMock);
        assertEquals("Yo", reply.getText());

    }

    @Test
    void testHandleDefaultMessage() {
        eventMock.getSource();
        eventMock.getTimestamp();

        verify(eventMock, atLeastOnce()).getSource();
        verify(eventMock, atLeastOnce()).getTimestamp();
    }
}
