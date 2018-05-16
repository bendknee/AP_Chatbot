package advprog.example.bot.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import advprog.example.bot.EventTestUtil;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest(properties = "line.bot.handler.enabled=false")
@ExtendWith(SpringExtension.class)
public class RecentTweetsControllerTest {

    static {
        System.setProperty("line.bot.channelSecret", "SECRET");
        System.setProperty("line.bot.channelToken", "TOKEN");
    }

    @Autowired
    private BotController botController;

    @Test
    void testValidAccountInput() {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/tweet recent twitterapi");

        TextMessage reply = botController.handleTextMessageEvent(event);
        String[] arrayOfTweets = reply.getText().split("\r\n");
        assertEquals(5, arrayOfTweets.length);
    }

    @Test
    void testEmptyAccountInput() {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/tweet recent GanAmpas");

        TextMessage reply = botController.handleTextMessageEvent(event);
        assertEquals("Hmm... seems like this account has no tweets yet.", reply.getText());
    }

    @Test
    void testInvalidAccountInput() {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/tweet recent aioefhey8wagruesfdskj");

        TextMessage reply = botController.handleTextMessageEvent(event);
        assertEquals("Sorry, this username is not available!", reply.getText());
    }
}