package advprog.example.bot.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.LocationMessageContent;
import com.linecorp.bot.model.event.source.UnknownSource;
import com.linecorp.bot.model.message.ImageMessage;

import com.linecorp.bot.model.message.template.ImageCarouselTemplate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest(properties = "line.bot.handler.enabled=false")
@ExtendWith(SpringExtension.class)
public class NearbyPhotosControllerTest {

    static {
        System.setProperty("line.bot.channelSecret", "SECRET");
        System.setProperty("line.bot.channelToken", "TOKEN");
    }

    @Autowired
    private NearbyPhotosController nearbyPhotosController;

    @Test
    void testContextLoads() {
        assertNotNull(nearbyPhotosController);
    }

    @Test
    void testHandleLocationMessageEvent() {
        MessageEvent<LocationMessageContent> event = new MessageEvent<LocationMessageContent>("4d0sah2z",
                new UnknownSource(), new LocationMessageContent("id69",
                "Fakultas Ilmu Komputer", "Kukusan Beji 14045",
                -6.364546, 106.828611), null);

        ImageCarouselTemplate response = nearbyPhotosController.handleLocationMessageEvent(event);

        assertEquals(response.getColumns().size(), 5);
    }
}
