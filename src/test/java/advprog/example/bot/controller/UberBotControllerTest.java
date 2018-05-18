package advprog.example.bot.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import advprog.example.bot.EventTestUtil;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest(properties = "line.bot.handler.enabled=false")
@ExtendWith(SpringExtension.class)
public class EchoControllerTest {

    static {
        System.setProperty("line.bot.channelSecret", "SECRET");
        System.setProperty("line.bot.channelToken", "TOKEN");
    }

    @Autowired
    private EchoController echoController;

    @Test
    void testContextLoads() {
        assertNotNull(echoController);
    }

    @Test
    void testHandleCommandUber() {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/uber");

        TextMessage reply = echoController.handleTextMessageEvent(event);
        String correctAnswer = "Destination: Plumpang (5 kilomerters from current position)\n"
                              + "Estimated travel time and fares for each Uber services:\n\n"
                              + "UberX (10 minutes, 10 rupiah)\n"
                              + "UberPool (10 minutes, 15 rupiah)\n"
                              + "UberBlack (10 minutes, 20 rupiah)\n"
                              + "UberMotor (10 minutes, 15 rupiah)\n\n"
                              + "Data provided by [Uber](https://www.uber.com)";

        assertEquals(correctAnswer, reply.getText());
    }

    @Test
    void testHandleCommandRemoveDestination() {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/remove_destination");

        TextMessage reply = echoController.handleTextMessageEvent(event);
        assertEquals("Destination removed", reply.getText());
    }

    @Test
    void testHandleCommandAddDestination() {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/add_destination");

        TextMessage reply = echoController.handleTextMessageEvent(event);
        assertEquals("Destination added", reply.getText());
    }

    @Test
    void testHandleDefaultMessage() {
        Event event = mock(Event.class);

        echoController.handleDefaultMessage(event);

        verify(event, atLeastOnce()).getSource();
        verify(event, atLeastOnce()).getTimestamp();
    }
}
