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

import org.junit.FixMethodOrder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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


    @Test
    void testHandleCommandUber() throws Exception {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/add_destination");
        uberBotController.handleTextMessageEvent(event);

        MessageEvent<LocationMessageContent> event2 =
                EventTestUtil.createDummyLocationMessage1();
        uberBotController.handleLocationMessageEvent(event2);


        MessageEvent<TextMessageContent> event3 =
                EventTestUtil.createDummyTextMessage("Uber HQ");
        uberBotController.handleTextMessageEvent(event3);


        MessageEvent<TextMessageContent> event4 =
                EventTestUtil.createDummyTextMessage("/uber");
        String reply = uberBotController.handleTextMessageEvent(event4);
        assertEquals("Perintah /uber diterima, silahkan kirim lokasi anda", reply);

        MessageEvent<LocationMessageContent> event5 =
                EventTestUtil.createDummyLocationMessage2();
        reply = uberBotController.handleLocationMessageEvent(event5);
        assertEquals("Lokasi diterima, silahkan pilih lokasi tujuan anda", reply);

        MessageEvent<TextMessageContent> event6 =
                EventTestUtil.createDummyTextMessage("Uber HQ");
        uberBotController.handleTextMessageEvent(event6);

        MessageEvent<TextMessageContent> event7 =
                EventTestUtil.createDummyTextMessage("/remove_destination");
        uberBotController.handleTextMessageEvent(event7);


        event7 = EventTestUtil.createDummyTextMessage("Uber HQ");
        uberBotController.handleTextMessageEvent(event7);


        event7 = EventTestUtil.createDummyTextMessage("yes");
        uberBotController.handleTextMessageEvent(event7);
    }


    @Test
    void testHandleCommandAddDestination() throws Exception {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/add_destination");

        String reply = uberBotController.handleTextMessageEvent(event);
        assertEquals("Perintah /add_destination diterima, silahkan kirim lokasi anda", reply);

        MessageEvent<LocationMessageContent> event2 =
                EventTestUtil.createDummyLocationMessage();
        reply = uberBotController.handleLocationMessageEvent(event2);
        assertEquals("Lokasi diterima, silahkan beri nama lokasi tersebut "
                + "(Contoh: Wisma Rossela)", reply);

        MessageEvent<TextMessageContent> event3 =
                EventTestUtil.createDummyTextMessage("Hotel");

        reply = uberBotController.handleTextMessageEvent(event3);
        assertEquals("Lokasi telah berhasil disimpan", reply);
    }

    @Test
    void testHandleCommandRemoveDestinationCancel() throws Exception {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/remove_destination");

        String reply = uberBotController.handleTextMessageEvent(event);
        assertEquals("Perintah /remove_destination diterima, silahkan "
                + "pilih lokasi yang ingin dihapus", reply);

        MessageEvent<TextMessageContent> event2 =
                EventTestUtil.createDummyTextMessage("Wisma Rossela");
        reply = uberBotController.handleTextMessageEvent(event2);
        assertEquals("Apakah anda yakin ingin menghapus lokasi? (yes/no)", reply);

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
        assertEquals("Perintah /remove_destination diterima, silahkan pilih"
                + " lokasi yang ingin dihapus", reply);

        MessageEvent<TextMessageContent> event2 =
                EventTestUtil.createDummyTextMessage("Hotel");
        reply = uberBotController.handleTextMessageEvent(event2);
        assertEquals("Apakah anda yakin ingin menghapus lokasi? (yes/no)", reply);

        MessageEvent<TextMessageContent> event3 =
                EventTestUtil.createDummyTextMessage("apa ini");

        reply = uberBotController.handleTextMessageEvent(event3);
        assertEquals("Apakah anda yakin ingin menghapus lokasi? (yes/no)", reply);

        MessageEvent<TextMessageContent> event4 =
                EventTestUtil.createDummyTextMessage("yes");

        reply = uberBotController.handleTextMessageEvent(event4);
        assertEquals("Lokasi telah dihapus", reply);
    }

    @Test
    void testHandleCommandRemoveDestinationDataIsEmpty() throws Exception {
        emptyData();
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/remove_destination");

        String reply = uberBotController.handleTextMessageEvent(event);

        assertEquals("Data destination kosong! Silahkan jalankan command /add_destination", reply);

        testHandleCommandAddDestination();
    }

    void emptyData() throws Exception {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/remove_destination");
        uberBotController.handleTextMessageEvent(event);
        event = EventTestUtil.createDummyTextMessage("Wisma Rossela");
        uberBotController.handleTextMessageEvent(event);
        event = EventTestUtil.createDummyTextMessage("yes");
        uberBotController.handleTextMessageEvent(event);
        event = EventTestUtil.createDummyTextMessage("/remove_destination");
        uberBotController.handleTextMessageEvent(event);
        event = EventTestUtil.createDummyTextMessage("Hotel");
        uberBotController.handleTextMessageEvent(event);
        event = EventTestUtil.createDummyTextMessage("yes");
        uberBotController.handleTextMessageEvent(event);
    }

    @Test
    void testHandleDefaultMessage() {
        Event event = mock(Event.class);

        uberBotController.handleDefaultMessage(event);

        verify(event, atLeastOnce()).getSource();
        verify(event, atLeastOnce()).getTimestamp();
    }
}
