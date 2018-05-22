package advprog.example.bot.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.linecorp.bot.model.message.TextMessage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EchoHandlerTest {
    private EchoHandler echoComposer;

    @BeforeEach
    void setUp() {
        this.echoComposer = new EchoHandler();
    }

    @Test
    public void testEchoComposeMessage() {
        String helloWorld = "Hello world!";
        TextMessage message = (TextMessage) this.echoComposer.composeReply(helloWorld);

        String resultMessage = message.getText();
        assertEquals(resultMessage, helloWorld);
    }
}
