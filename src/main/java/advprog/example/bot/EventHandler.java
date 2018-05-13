package advprog.example.bot;

import advprog.example.bot.context.Context;
import advprog.example.bot.composer.EchoComposer;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.ImageMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import java.util.logging.Logger;

@LineMessageHandler
public class EventHandler {
    private static final Logger LOGGER = Logger.getLogger(EchoComposer.class.getName());
    private static final String NO_COMMAND_REPLY = "No command found. " + CommandPattern.getCommands();

    @EventMapping
    public Message handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        Message reply = CommandPattern.getMessageFromEvent(event);

        return reply == null ? new TextMessage(NO_COMMAND_REPLY) : reply;
    }

    @EventMapping
    public void handleImageMessageEvent(MessageEvent<ImageMessageContent> event) {
        String userId = event.getSource().getUserId();
        String imageId = event.getMessage().getId();

        Context.storeContext(userId, imageId);
    }

    @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));
    }
}