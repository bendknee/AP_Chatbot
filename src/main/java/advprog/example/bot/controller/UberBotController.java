package advprog.example.bot.controller;

import com.linecorp.bot.model.action.MessageAction;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.template.CarouselColumn;
import com.linecorp.bot.model.message.template.CarouselTemplate;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Logger;

@LineMessageHandler
public class UberBotController {

    private static final Logger LOGGER = Logger.getLogger(UberBotController.class.getName());
    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));
        TextMessageContent content = event.getMessage();
        String contentText = content.getText();
        String reply = "";
        if (contentText == "/uber") {
          reply = uberCommand();
        } else if (contentText == "/add_destination") {
          reply = addDestinationCommand();
        } else if (contentText == "remove_destination") {
          reply = removeDestinationCommand();
        }

        return new TextMessage("");
    }

    private String uberCommand() {
      return "";
    }

    private String addDestinationCommand() {
      return "";
    }

    private String removeDestinationCommand() {
      return "";
    }

    @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));
    }
}
