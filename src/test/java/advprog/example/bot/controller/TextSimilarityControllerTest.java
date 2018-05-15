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
public class TextSimilarityControllerTest {

    static {
        System.setProperty("line.bot.channelSecret", "SECRET");
        System.setProperty("line.bot.channelToken", "TOKEN");
    }

    @Autowired
    private TextSimilarityController textSimilarityController;

    @Test
    void testContextLoads() {
        assertNotNull(textSimilarityController);
    }

    @Test
    void testTextSimilarityCorrect() {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/docs_sim "
                        + "'Cameron wins the Oscar' "
                        + "'All nominees for the Academy Awards'");

        TextMessage reply = textSimilarityController.handleTextMessageEvent(event);

        assertEquals("72.01%", reply.getText());
    }

    @Test
    void testTextSimilarityDocSim() {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("Haii namaku John Cena");

        TextMessage reply = textSimilarityController.handleTextMessageEvent(event);

        assertEquals("Untuk membandingan 2 text dokumen, balas dengan "
                + "/docs_sim 'TEXT1' 'TEXT2' atau /docs_sim URL1 URL2", reply.getText());
    }

    @Test
    void testTextSimilarityCatchError() {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/docs_sim asd asd");

        TextMessage reply = textSimilarityController.handleTextMessageEvent(event);

        assertEquals("ERROR! Untuk membandingan 2 text dokumen, balas dengan "
                + "/docs_sim 'TEXT1' 'TEXT2' atau /docs_sim URL1 URL2", reply.getText());
    }

    @Test
    void testTextSimilarityFalse() {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/docs_sim");

        TextMessage reply = textSimilarityController.handleTextMessageEvent(event);

        assertEquals("Untuk membandingan 2 text dokumen, balas dengan "
                + "/docs_sim 'TEXT1' 'TEXT2' atau /docs_sim URL1 URL2", reply.getText());
    }

    @Test
    void testTextSimilarityCorrectUrl() {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/docs_sim "
                        + "http://www.bbc.com/news/world-us-canada-26734036 "
                        + "http://edition.cnn.com/2014/03/24/politics/obama-europe-trip/");

        TextMessage reply = textSimilarityController.handleTextMessageEvent(event);

        assertEquals("63.31%", reply.getText());
    }

    @Test
    void testTextSimilaritySameText() {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/docs_sim "
                        + "'Cameron wins the Oscar' "
                        + "'Cameron wins the Oscar'");

        TextMessage reply = textSimilarityController.handleTextMessageEvent(event);

        assertEquals("100%", reply.getText());
    }

    @Test
    void testTextSimilarityZeroSimilarity() {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/docs_sim "
                        + "'I do not feel so good' "
                        + "'Food is love'");

        TextMessage reply = textSimilarityController.handleTextMessageEvent(event);

        assertEquals("0%", reply.getText());
    }

    @Test
    void testHandleDefaultMessage() {
        Event event = mock(Event.class);

        textSimilarityController.handleDefaultMessage(event);

        verify(event, atLeastOnce()).getSource();
        verify(event, atLeastOnce()).getTimestamp();
        
    }
}
