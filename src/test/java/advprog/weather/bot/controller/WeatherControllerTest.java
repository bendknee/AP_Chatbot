package advprog.weather.bot.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import advprog.example.bot.EventTestUtil;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.LocationMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@SpringBootTest(properties = "line.bot.handler.enabled=false")
@ExtendWith(SpringExtension.class)
public class WeatherControllerTest {

    static {
        System.setProperty("line.bot.channelSecret", "SECRET");
        System.setProperty("line.bot.channelToken", "TOKEN");
    }

    @Autowired
    private WeatherController weatherController;

    @Test
    void testContextLoads() {
        assertNotNull(weatherController);
    }

    @Test
    public void testSlashWeather() {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createPrivateTextMessage("/weather");
        TextMessage reply = weatherController.handleTextMessageEvent(event);

        assertTrue(reply.getText().contains("Please submit a location"));
    }

    @Test
    public void testWeatherInPrivate() {
        WeatherController controller = new WeatherController();
        MessageEvent<TextMessageContent> event1 =
                EventTestUtil.createPrivateTextMessage("/weather");
        controller.handleTextMessageEvent(event1);
        MessageEvent<LocationMessageContent> event2 =
                EventTestUtil.createDummyLocationMessage(-6.369084, 106.832578);
        TextMessage reply = controller.handleLocationMessageEvent(event2);
        assertTrue(reply.getText().contains("Depok, Indonesia"));
    }

    @Test
    public void testWeatherInGroup() {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createGroupTextMessage("cuaca di Brussels");
        TextMessage reply = weatherController.handleTextMessageEvent(event);
        assertTrue(reply.getText().contains("Belgium"));

        event = EventTestUtil.createGroupTextMessage("eh cuaca di Pyongyang apa ya?");
        reply = weatherController.handleTextMessageEvent(event);
        assertTrue(reply.getText().contains("Korea"));

        event = EventTestUtil.createGroupTextMessage("pengen tau cuaca di Canberra dong");
        reply = weatherController.handleTextMessageEvent(event);
        assertTrue(reply.getText().contains("Australia"));
    }

    @Test
    public void testInvalidCity() {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createGroupTextMessage("cuaca di Wakanda ada gak?");
        TextMessage reply = weatherController.handleTextMessageEvent(event);
        assertEquals("City not found. Try another city", reply.getText());
    }
}
