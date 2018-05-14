package advprog.example.bot.composer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.linecorp.bot.model.message.TextMessage;
import org.junit.jupiter.api.Test;

class EchoComposerTest {

    @Test
    public void testEchoComposeMessage() {
        String helloWorld = "Hello world!";
        TextMessage message = (TextMessage) EchoComposer.composeMessage(helloWorld);

        String resultMessage = message.getText();
        assertEquals(resultMessage, helloWorld);
    }
}