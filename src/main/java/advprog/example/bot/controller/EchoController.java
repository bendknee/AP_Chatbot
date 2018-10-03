package advprog.example.bot.controller;

import advprog.example.bot.anime.AnimeBot;
import advprog.example.bot.anime.ListAnimeBot;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.source.UserSource;
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

        if (contentText.toLowerCase().contains("hari ini nonton apa?")
                && event.getSource().getClass() != UserSource.class) {
            ListAnimeBot lab = new ListAnimeBot();
            return new TextMessage(lab.getListAnime());
        } else if (contentText.toLowerCase().contains("hari ini nonton apa?")) {
            return new TextMessage("This command should be on group chats");
        }

        String[] splitContent = contentText.split(" ", 2);
        String command = splitContent[0];

        if (splitContent.length < 2) {
            return handleDefaultMessage(event);
        }

        String input = splitContent[1];

        if (command.equals("/echo")) {
            return new TextMessage(input);
        } else if (command.equals("/is_airing")) {
            AnimeBot ab = new AnimeBot(input);
            return new TextMessage(ab.getStatusMessage());
        }

        return handleDefaultMessage(event);

    }

    @EventMapping
    public TextMessage handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));

        System.out.println("default handle event: " + event);

        return new TextMessage("Wrong command!\nTry :\n/echo STRING\n/is_airing ANIME");
    }
}
