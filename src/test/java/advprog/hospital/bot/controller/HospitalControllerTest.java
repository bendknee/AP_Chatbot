package advprog.hospital.bot.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import advprog.hospital.bot.EventTestUtil;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.LocationMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.LocationMessage;
import com.linecorp.bot.model.message.TextMessage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest(properties = "line.bot.handler.enabled=false")
@ExtendWith(SpringExtension.class)
public class HospitalControllerTest {

    static {
        System.setProperty("line.bot.channelSecret", "SECRET");
        System.setProperty("line.bot.channelToken", "TOKEN");
    }

    @Autowired
    private HospitalController hospitalController;

    @Test
    void testContextLoads() {
        assertNotNull(hospitalController);
    }

    @Test
    void testHandleTextMessageEvent() {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/info");

        String reply = hospitalController.handleTextMessageEvent(event);

        assertEquals("Terdapat 10 rumah sakit sekitar Depok dalam database", reply);
    }

    @Test
    void testHandleLocationMessageEvent() {
        MessageEvent<LocationMessageContent> event =
                EventTestUtil.createDummyLocationMessage(-6.362413, 106.818845);

        String reply = hospitalController.handleLocationMessageEvent(event);

        assertEquals("Mohon ulangi permintaan Anda", reply);
    }

    @Test
    void testHandleHospitalFromLocation() {
        MessageEvent<TextMessageContent> event0 =
                EventTestUtil.createDummyTextMessage("/hospital");
        MessageEvent<TextMessageContent> event1 =
                EventTestUtil.createDummyTextMessage("/hospital");
        MessageEvent<LocationMessageContent> event2 =
                EventTestUtil.createDummyLocationMessage(-6.362413, 106.818845);

        String reply0 = hospitalController.handleTextMessageEvent(event0);
        String reply1 = hospitalController.handleTextMessageEvent(event1);
        String reply2 = hospitalController.handleLocationMessageEvent(event2);

        assertEquals("Terima kasih, permintaan anda akan kami proses", reply0);
        assertEquals("Silahkan masukkan lokasi", reply1);
        assertEquals("Mohon tunggu, permintaan Anda sedang kami proses", reply2);
    }

    @Test
    void testHandleHospitalCarousel() {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/random_hospital");

        String reply = hospitalController.handleTextMessageEvent(event);

        assertEquals("Method dapat dijalankan tanpa mengeluarkan error", reply);
    }

    @Test
    void testHandleGetFromCarousel() {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/get 0");

        String reply = hospitalController.handleTextMessageEvent(event);

        assertEquals("Method dapat dijalankan tanpa mengeluarkan error", reply);
    }

    @Test
    void testDaruratMessage() {
        MessageEvent<TextMessageContent> event0 =
                EventTestUtil.createDummyTextMessage("Waduh, darurat bosku");
        MessageEvent<LocationMessageContent> event1 =
                EventTestUtil.createDummyLocationMessage(-6.362413, 106.818845);

        hospitalController.handleTextMessageEvent(event0);
        String reply = hospitalController.handleLocationMessageEvent(event1);

        assertEquals("Mohon tunggu, permintaan Anda sedang kami proses", reply);
    }

    @Test
    void testHandleDefaultMessage() {
        Event event = mock(Event.class);

        hospitalController.handleDefaultMessage(event);

        verify(event, atLeastOnce()).getSource();
        verify(event, atLeastOnce()).getTimestamp();
    }
}
