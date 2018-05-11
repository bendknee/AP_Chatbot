package advprog.example.bot.command;

import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;

import java.util.Optional;

public class SentimentCommand {

    public static String HELP_TEXT = "Usage: /sentiment [text]";

    public static Message execute(String args) {
        // TODO: Implement
        return new TextMessage("sentiment command");
    }

}
