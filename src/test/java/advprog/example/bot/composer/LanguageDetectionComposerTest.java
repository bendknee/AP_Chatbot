package advprog.example.bot.composer;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.linecorp.bot.model.message.TextMessage;
import org.junit.jupiter.api.Test;

public class LanguageDetectionComposerTest {

    @Test
    void testDetectLangComposeMessage() {
        String helloWorld = "Hello world!";
        String errorMessage = "Something is wrong with our 3rd party server. "
                + "Please try again later.";
        TextMessage message = (TextMessage) LanguageDetectionComposer.composeMessage(helloWorld);

        assertNotEquals(errorMessage, message.getText());
    }
}
