package advprog.example.bot;

import advprog.example.bot.composer.Composer;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandPattern {
    private static HashMap<Pattern, Composer> store = new HashMap<>();
    private static String commands = "";

    private CommandPattern() {}

    public static void addPattern(String command, Composer composer) {
        Pattern pattern = Pattern.compile("(" + command + ")(.*)");

        if (!commands.equals("")) {
            commands += ", ";
        }

        commands += command;
        store.put(pattern, composer);
    }

    public static Message getMessageFromEvent(MessageEvent<TextMessageContent> event) {
        Message message = null;
        String command = event.getMessage().getText();
        String userId = event.getSource().getUserId();

        for (Pattern pattern: store.keySet()) {
            Matcher matcher = pattern.matcher(command);

            if (matcher.matches()) {
                message = store.get(pattern).composeMessage(command, userId);

                break;
            }
        }

        return message;
    }

    public static String getCommands() {
        return "Available commands : " + commands;
    }
}
