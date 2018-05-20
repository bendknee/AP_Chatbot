package advprog.example.bot.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import advprog.example.bot.EventTestUtil;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.LocationMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;

import org.junit.FixMethodOrder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;

@SpringBootTest(properties = "line.bot.handler.enabled=false")
@ExtendWith(SpringExtension.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UberBotControllerTest {

    static {
        System.setProperty("line.bot.channelSecret", "SECRET");
        System.setProperty("line.bot.channelToken", "TOKEN");
    }

    @Autowired
    private UberBotController uberBotController;

    @Test
    void testContextLoads() {
        assertNotNull(uberBotController);
    }

    /*
    @Test
    void testHandleCommandUber() throws Exception {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/uber");

        String reply = uberBotController.handleTextMessageEvent(event);
        String correctAnswer = "Destination: Plumpang (5 kilomerters from current position)\n"
                              + "Estimated travel time and fares for each Uber services:\n\n"
                              + "UberX (10 minutes, 10 rupiah)\n"
                              + "UberPool (10 minutes, 15 rupiah)\n"
                              + "UberBlack (10 minutes, 20 rupiah)\n"
                              + "UberMotor (10 minutes, 15 rupiah)\n\n"
                              + "Data provided by [Uber](https://www.uber.com)";

        assertEquals(correctAnswer, reply);
    }
    */

    @Test
    void testHandleCommandAddDestination() throws Exception {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/add_destination");

        String reply = uberBotController.handleTextMessageEvent(event);
        assertEquals("Perintah /add_destination diterima, silahkan kirim lokasi anda", reply);

        MessageEvent<LocationMessageContent> event2 =
                EventTestUtil.createDummyLocationMessage();
        reply = uberBotController.handleLocationMessageEvent(event2);
        assertEquals("Lokasi diterima, silahkan beri nama lokasi tersebut (Contoh: Wisma Rossela)", reply);

        MessageEvent<TextMessageContent> event3 =
                EventTestUtil.createDummyTextMessage("Wisma Rossela");

        reply = uberBotController.handleTextMessageEvent(event3);
        assertEquals("Lokasi telah berhasil disimpan", reply);
    }

    @Test
    void testHandleCommandRemoveDestinationCancel() throws Exception {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/remove_destination");

        String reply = uberBotController.handleTextMessageEvent(event);
        assertEquals("Perintah /remove_destination diterima, silahkan pilih lokasi yang ingin dihapus", reply);

        MessageEvent<TextMessageContent> event2 =
                EventTestUtil.createDummyTextMessage("Wisma Rossela");
        reply = uberBotController.handleTextMessageEvent(event2);
        assertEquals("Apakah anda yakin ingin menhapus lokasi? (yes/no)", reply);

        MessageEvent<TextMessageContent> event3 =
                EventTestUtil.createDummyTextMessage("No");

        reply = uberBotController.handleTextMessageEvent(event3);
        assertEquals("Lokasi tidak dihapus", reply);
    }

    @Test
    void testHandleCommandRemoveDestination() throws Exception {

        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/remove_destination");

        String reply = uberBotController.handleTextMessageEvent(event);
        assertEquals("Perintah /remove_destination diterima, silahkan pilih lokasi yang ingin dihapus", reply);

        MessageEvent<TextMessageContent> event2 =
                EventTestUtil.createDummyTextMessage("Wisma Rossela");
        reply = uberBotController.handleTextMessageEvent(event2);
        assertEquals("Apakah anda yakin ingin menhapus lokasi? (yes/no)", reply);

        MessageEvent<TextMessageContent> event3 =
                EventTestUtil.createDummyTextMessage("apa ini");

        reply = uberBotController.handleTextMessageEvent(event3);
        assertEquals("Apakah anda yakin ingin menhapus lokasi? (yes/no)", reply);

        MessageEvent<TextMessageContent> event4 =
                EventTestUtil.createDummyTextMessage("yes");

        reply = uberBotController.handleTextMessageEvent(event4);
        assertEquals("Lokasi telah dihapus", reply);
    }

    @Test
    void testHandleCommandRemoveDestinationDataIsEmpty() throws Exception {
        testHandleCommandRemoveDestination();
        testHandleCommandRemoveDestination();

        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/remove_destination");

        String reply = uberBotController.handleTextMessageEvent(event);

        assertEquals("Data destination kosong! Silahkan jalankan command /add_destination", reply);

        testHandleCommandAddDestination();
    }

    @Test
    void testHandleDefaultMessage() {
        Event event = mock(Event.class);

        uberBotController.handleDefaultMessage(event);

        verify(event, atLeastOnce()).getSource();
        verify(event, atLeastOnce()).getTimestamp();
    }
}
