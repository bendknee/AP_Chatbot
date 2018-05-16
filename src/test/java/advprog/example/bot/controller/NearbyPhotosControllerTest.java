package advprog.example.bot.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import advprog.example.bot.EventTestUtil;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.LocationMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.template.ImageCarouselTemplate;
import java.util.List;
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
    public void testContextLoads() {
        assertNotNull(nearbyPhotosController);
    }

    @Test
    public void testUrbanAreaLocationMessage() {
        MessageEvent<LocationMessageContent> event =
                EventTestUtil.createDummyLocationMessage(-6.369084, 106.832578);
        List<Message> responseMessage = nearbyPhotosController.handleLocationMessageEvent(event);
        assertTrue(responseMessage.size() > 0);
        TemplateMessage message = (TemplateMessage) responseMessage.get(0);
        ImageCarouselTemplate carousel = (ImageCarouselTemplate) message.getTemplate();
        assertTrue(carousel.getColumns().size() > 0);
    }

    @Test
    public void testRuralAreaLocationMessage() {
        MessageEvent<LocationMessageContent> event =
                EventTestUtil.createDummyLocationMessage(-6.160657, 107.423090);
        List<Message> responseMessage = nearbyPhotosController.handleLocationMessageEvent(event);
        assertTrue(responseMessage.size() > 0);
        TextMessage message = (TextMessage) responseMessage.get(0);
        assertEquals(message.getText(), "No image found. Perhaps select a more populous location.");
    }

    @Test
    public void testKeywordMessageTrigger() {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("nearby photos");
        List<TextMessage> responseMessage = nearbyPhotosController.handleKeywordTrigger(event);
        assertTrue(responseMessage.size() > 0);
        TextMessage textMessage = responseMessage.get(0);
        assertTrue(textMessage.getText().contains("'Share location' feature below"));
    }
}
