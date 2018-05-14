package advprog.example.bot.composer;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.linecorp.bot.model.message.TextMessage;
import org.junit.jupiter.api.Test;

class FakeJsonComposerTest {

    @Test
    void testFakeJsonComposeMessage() {
        TextMessage message = (TextMessage) FakeJsonComposer.composeMessage();
        String replyTextContent = message.getText();

        assertTrue(replyTextContent.contains("id"));
    }
}