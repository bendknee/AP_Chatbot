package advprog.example.bot;

import advprog.example.bot.composer.Composer;
import advprog.example.bot.watcher.Watcher;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandPattern {
    private static HashMap<Pattern, Composer> composerStore = new HashMap<>();
    private static HashMap<Pattern, Watcher> watcherStore = new HashMap<>();
    private static String commands = "";

    private CommandPattern() {}

    public static void addWatcher(Pattern pattern, Watcher watcher) {
        watcherStore.put(pattern, watcher);
    }

    public static void addPattern(String command, Composer composer) {
        Pattern pattern = Pattern.compile("(" + command + ")(.*)");

        if (!commands.equals("")) {
            commands += ", ";
        }

        commands += command;
        composerStore.put(pattern, composer);
    }

    public static Message getMessageFromEvent(MessageEvent<TextMessageContent> event) {
        Message message = null;
        String command = event.getMessage().getText();
        String userId = event.getSource().getUserId();

        for (Pattern pattern: composerStore.keySet()) {
            Matcher matcher = pattern.matcher(command);

            if (matcher.matches()) {
                String arg = matcher.group(2);
                message = composerStore.get(pattern).composeMessage(arg, userId);

                break;
            }
        }

        return message;
    }

    public static void notifyWatcher(MessageEvent<TextMessageContent> event) {
        String message = event.getMessage().getText();

        for (Pattern pattern: watcherStore.keySet()) {
            Matcher matcher = pattern.matcher(message);

            if (matcher.matches()) {
                watcherStore.get(pattern).handle(event);
            }
        }
    }

    public static String getCommands() {
        return "Available commands : " + commands;
    }
}
