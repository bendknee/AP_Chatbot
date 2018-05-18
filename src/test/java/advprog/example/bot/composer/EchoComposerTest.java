package advprog.example.bot.composer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.linecorp.bot.model.message.TextMessage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EchoComposerTest {
    private EchoComposer echoComposer;

    @BeforeEach
    void setUp() {
        this.echoComposer = new EchoComposer();
    }

    @Test
    public void testEchoComposeMessage() {
        String helloWorld = "Hello world!";
        TextMessage message = (TextMessage) this.echoComposer.composeMessage(helloWorld);

        String resultMessage = message.getText();
        assertEquals(resultMessage, helloWorld);
    }
}
