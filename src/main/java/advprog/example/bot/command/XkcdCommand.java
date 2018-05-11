package advprog.example.bot.command;

import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;

import java.util.Optional;

public class XkcdCommand {

    public static String HELP_TEXT = "Usage: /xkcd [id]";

    public static Message execute(String args) {
        // TODO: Implement
        return new TextMessage("xkcd command");
    }


    private static Optional<String> getXkcdComidById(int id) {
        // TODO: Implement
        return Optional.of("stub url");
    }
}
