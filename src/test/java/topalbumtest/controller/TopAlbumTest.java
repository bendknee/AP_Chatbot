package topalbumtest.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import advprog.example.bot.EventTestUtil;
import topalbumpac.TopAlbumPac;
import topalbumpac.controller.TopAlbumsControl;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest(properties = "line.bot.handler.enabled=false", classes = TopAlbumPac.class)
@ExtendWith(SpringExtension.class)
public class TopAlbumTest {

    static {
        System.setProperty("line.bot.channelSecret", "9e514c1bbfd82d"
                + "65c9c62738e335ad0c");
        System.setProperty("line.bot.channelToken", "ZBfd4J0OQJsmM96kLQv8lJ"
                + "Z6HROSPl082xGLEE1Mhmc5gofdxygtkYDkErflbC4hi89cYnBU"
                + "L139T8yemI0yjX9J5LeWsWf1LDJviTYReTkbl15Pu1Kje9"
                + "wBce9VP1hEtIoARacjxCQJu9hRj5iy3wd"
                + "B04t89/1O/w1cDnyilFU=");
    }

    private TopAlbumsControl control = new TopAlbumsControl();

    @Test
    void testContextLoads() {
        assertNotNull(control);
    }
}
