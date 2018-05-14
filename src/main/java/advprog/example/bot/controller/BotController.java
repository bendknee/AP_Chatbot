package advprog.example.bot.controller;

import advprog.example.bot.Command;
import advprog.example.bot.echo.EchoCommand;
import advprog.example.bot.twitter.RecentTweetsCommand;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@LineMessageHandler
public class BotController {

    private static final Logger LOGGER = Logger.getLogger(BotController.class.getName());
    private Map<String, Command> textCommands = new HashMap<>();

    public BotController() {
        initializeTextCommands();
    }

    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));

        TextMessageContent content = event.getMessage();
        String contentText = content.getText();

        TextMessage reply = null;
        for (String pattern : textCommands.keySet()) {
            if (contentText.matches(pattern)) {
                reply = (TextMessage) textCommands.get(pattern).produceMessage(content);
            }
        }

        return reply;
    }

    @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));
    }

    public void initializeTextCommands() {
        textCommands.put("^/echo .*", new EchoCommand());
        textCommands.put("^/tweets recent .*", new RecentTweetsCommand());
    }
}
