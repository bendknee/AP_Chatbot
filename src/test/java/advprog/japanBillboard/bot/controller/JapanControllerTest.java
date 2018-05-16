package advprog.japanBillboard.bot.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


import advprog.japanBillboard.bot.EventTestUtil;
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
public class JapanControllerTest {

    static {
        System.setProperty("line.bot.channelSecret", "SECRET");
        System.setProperty("line.bot.channelToken", "TOKEN");
    }

    @Autowired
    private JapanController japanController;

    @Test
    void testContextLoads() {
        assertNotNull(japanController);
    }

    @Test
    void testHandleTextMessageEvent() {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/billboard japan100");

        TextMessage reply = japanController.handleTextMessageEvent(event);

        assertEquals("(1) Hayaokuri Calendar - HKT48\n" +
                "(2) Syncronicity - Nogizaka46\n" +
                "(3) KISS is my life. - SingTuyo\n" +
                "(4) Lemon - Kenshi Yonezu\n" +
                "(5) Odd Future - UVERworld\n" +
                "(6) Straw - Aiko\n" +
                "(7) Kono Michi Wo - Kazumasa Oda\n" +
                "(8) Glass Wo Ware! - Keyakizaka46\n" +
                "(9) What Is Love? - TWICE\n" +
                "(10) Play A Love Song - Hikaru Utada\n", reply.getText());
    }

    @Test
    void testHandleTextMessageEventInput(){
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/testestes");

        TextMessage reply = japanController.handleTextMessageEvent(event);

        assertEquals("Inputan tidak tersedia, coba /billboard japan100", reply.getText());
    }

    @Test
    void testHandleDefaultMessage() {
        Event event = mock(Event.class);

        japanController.handleDefaultMessage(event);

        verify(event, atLeastOnce()).getSource();
        verify(event, atLeastOnce()).getTimestamp();
    }
}