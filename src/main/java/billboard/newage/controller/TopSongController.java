package billboard.newage.controller;

import billboard.newage.parser.HtmlParser;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import java.util.ArrayList;
import java.util.logging.Logger;

@LineMessageHandler
public class TopSongController {

    private static final Logger LOGGER = Logger.getLogger(TopSongController.class.getName());

    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));
        TextMessageContent content = event.getMessage();
        String contentText = content.getText();
        return stringBuilderForEvents(contentText);
    }

    private TextMessage stringBuilderForEvents(String contentText) {
        if (contentText.equals("/billboard newage")) {
            HtmlParser parser = new HtmlParser();
            ArrayList<String> arrArtist = parser.getArrayArtist();
            ArrayList<String> arrSong = parser.getArraySong();
            String builder = "";
            for (int i = 0; i < 10; i++) {
                builder += "(" + (i + 1) + ") " + arrArtist.get(i) + " - "
                        + arrSong.get(i) + "\n";
            }
            return new TextMessage(builder);
        }
        return new TextMessage("");
    }

    @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));
    }
}