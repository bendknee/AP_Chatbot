package advprog.example.bot.controller;

import advprog.example.bot.manager.GuessAcronymsManager;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;

@LineMessageHandler
public class GuessAcronymsController {

    private static final Logger LOGGER = Logger.getLogger(GuessAcronymsController.class.getName());

    private GuessAcronymsManager guessAcronymsManager;

    @Autowired
    public GuessAcronymsController(GuessAcronymsManager guessAcronymsManager) {
        this.guessAcronymsManager = guessAcronymsManager;
    }

    @EventMapping
    public void handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')",
                                  event.getTimestamp(), event.getMessage()));
    }
}
