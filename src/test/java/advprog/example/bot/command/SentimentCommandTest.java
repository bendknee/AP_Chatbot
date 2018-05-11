package advprog.example.bot.command;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.linecorp.bot.model.message.TextMessage;

import com.linecorp.bot.model.message.Message;


public class SentimentCommandTest {

    @Test
    void testExecuteOk() {
        Message reply = SentimentCommand.execute("This is a test.");
        TextMessage replyText = (TextMessage)reply;

        String text = replyText.getText();

        assertTrue(text.contains("Sentiment: "));
    }


}
