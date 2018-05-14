package billboardpackage.controller;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import java.io.IOException;

import java.util.logging.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@LineMessageHandler
public class BillboardController {
    private static final Logger LOGGER = Logger.getLogger(BillboardController.class.getName());

    static {
        System.setProperty("line.bot.channelSecret", "68d4dc2b82c35"
                + "d6e24eadf8bfea40fa2\n");
        System.setProperty("line.bot.channelToken", "JLOZx62DldFCg59q3tbHAt"
                + "hoXwCgEpTBYQk194MZZnUGjkCTdANhwjeBjX/GjRkSDU"
                + "bW60uDeWiQhJPPOetPB9y/7V1q7SqvhyABn8RRkLFWI2o"
                + "+bewYl+5qazqJJEYfahfQWEXKqA2sQf+3df"
                + "ruFgdB04t89/1O/w1cDnyilFU=");
    }

    @EventMapping
    public TextMessage
        handleTextMessageEvent(MessageEvent<TextMessageContent> event) throws IOException {
        LOGGER.fine(String.format("TextMessageContent"
                        + "(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));
        TextMessageContent content = event.getMessage();
        String textContext = content.getText();
        if (textContext.length() < 18) {
            return new TextMessage("Sorry input not valid the format "
                    + "should be /billboard bill200 ArtistName");
        }
        String parser = textContext.substring(0, 18);
        String artist = textContext.substring(19, textContext.length());
        try {
            if (!parser.equals("/billboard bill200")) {
                throw new IllegalArgumentException();
            }
            String result = cekArtis(artist);
            if (result.equalsIgnoreCase("")) {
                return new TextMessage("Sorry, Artist "
                        + artist + " is not in the chart");
            }
            return new TextMessage(result);
        } catch (IllegalArgumentException e) {
            return new TextMessage("Sorry, Artist "
                    + textContext + " is not available");
        }
    }


    @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));
    }

    public static String cekArtis(String artis) throws IOException {
        Document doc = Jsoup.connect("https://www"
                + ".billboard.com/charts/billboard-200").get();
        Elements containers = doc.select(".chart-row__title");
        String hasil = "";
        for (int i = 0; i < 200; i++) {
            Element elements = containers.get(i);
            if (elements.select(".chart-row__artist")
                    .text().equalsIgnoreCase(artis)) {
                hasil += "\n" + elements
                        .select(".chart-row__artist").text()
                        + "\n" + elements.select(".chart-row__song").text()
                        + "\n" + "Position : " + (i + 1) + "\n";
            }
        }
        return hasil;
    }
}
