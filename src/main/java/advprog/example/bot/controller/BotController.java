package advprog.example.bot.controller;

import advprog.example.bot.composer.EchoComposer;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.logging.Logger;

@LineMessageHandler
public class BotController {

    private static final Logger LOGGER = Logger.getLogger(BotController.class.getName());
    private LineMessagingClient lineMessagingClient;
    private EchoComposer echoComposer;

    @Autowired
    public BotController(
            LineMessagingClient lineMessagingClient,
            EchoComposer echoComposer
    ) {
        this.lineMessagingClient = lineMessagingClient;
        this.echoComposer = echoComposer;
    }

    @EventMapping
    public Message handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));

        TextMessageContent content = event.getMessage();
        String contentText = content.getText();

        Message reply = null;

        if (contentText.matches("^/echo .*")) {
            contentText = contentText.replace("/echo ", "");
            reply = this.echoComposer.composeMessage(contentText);
        }

        return reply;
    }

    @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));
    }
}
