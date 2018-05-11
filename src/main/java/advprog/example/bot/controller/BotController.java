package advprog.example.bot.controller;

import advprog.example.bot.command.XkcdCommand;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.ImageMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import java.util.logging.Logger;

@LineMessageHandler
public class BotController {

    private static final Logger LOGGER = Logger.getLogger(BotController.class.getName());

    @EventMapping
    public Message handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        System.out.println("event: " + event);

        TextMessageContent messageContent = event.getMessage();
        String message = messageContent.getText();
        String[] splitMsg = message.split(" ", 2);
        String command = splitMsg[0];
        if (splitMsg.length < 2) {
            return handleDefaultMessageEvent(event);
        }

        String args = splitMsg[1];
        switch (command) {
            case "/echo":
                return new TextMessage(args);
            case "/xkcd":
                return XkcdCommand.execute(args);
            default:
                return handleDefaultMessageEvent(event);
        }
    }

    @EventMapping
    public Message handleDefaultMessageEvent(Event event) {
        System.out.println("default handle event: " + event);

        return new TextMessage("Invalid command");
    }
}
