package advprog.example.bot.controller;

import advprog.example.bot.NewAgeBot.NewAgeBot;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import java.util.logging.Logger;

@LineMessageHandler
public class EchoController {

    private static final Logger LOGGER = Logger.getLogger(EchoController.class.getName());

    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));

        TextMessageContent content = event.getMessage();
        String contentText = content.getText();

        String[] splitContent = contentText.split(" ", 2);
        String command = splitContent[0];

        if (splitContent.length < 2) {
            return handleDefaultMessage(event);
        }

        String input = splitContent[1];


        if(command.equals("/echo")) {
            return new TextMessage(input);
        } else if (command.equals("/billboard")) {
            String[] input2 = input.split(" ", 2);

            String command2 = input2[0];
            if(command2.toLowerCase().equals("newage")) {
                String artist = input2[1];
                NewAgeBot nab = new NewAgeBot(artist);
                return new TextMessage(nab.getFavArtist());
            }
        }

        return handleDefaultMessage(event);

    }


    @EventMapping
    public TextMessage handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));

        System.out.println("default handle event: " + event);

        return new TextMessage("Invalid command");
    }


}
