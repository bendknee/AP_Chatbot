package advprog.example.bot.command;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.message.ImageMessage;
import com.linecorp.bot.model.message.TextMessage;

import com.linecorp.bot.model.message.Message;


public class XkcdCommandTest {

    @Test
    void testExecuteOk() {
        Message message = XkcdCommand.execute("1");
        ImageMessage messageImage = (ImageMessage)message;

        assertEquals("https://imgs.xkcd.com/comics/barrel_cropped_(1).jpg", messageImage.getOriginalContentUrl());
        assertEquals("https://imgs.xkcd.com/comics/barrel_cropped_(1).jpg", messageImage.getPreviewImageUrl());
    }

    @Test
    void testExecuteIdNotFound() {
        Message message = XkcdCommand.execute("-1");
        TextMessage messageText = (TextMessage)message;

        assertEquals("ID not found", messageText.getText());
    }

    @Test
    void testExecuteInvalidId() {
        Message message = XkcdCommand.execute("abc");
        TextMessage messageText = (TextMessage)message;

        assertEquals(XkcdCommand.HELP_TEXT, messageText.getText());
    }

}
