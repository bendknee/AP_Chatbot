package tropicalbot.controller;

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
public class TropicalController {

    private static final Logger LOGGER = Logger.getLogger(TropicalController.class.getName());

    static {
        System.setProperty("line.bot.channelSecret", "4f5061aa776591"
                + "aad1bf35965ab5f25d");
        System.setProperty("line.bot.channelToken", "csN67un3gG09L80xWS5VjCcb0OM"
                + "3GqOpQjBd76HuJn1Go8Wwb4xQbPK9kRygi144i9dsvFGc6OUgFiHCdJfxcen"
                + "uByIV0ASTfk6xIxLwoC9fGE9+lqF/frcVm0AQUmukpJ1wR2kl"
                + "5+1b9t7Pdf6fdgdB04t89/1O/w1cDnyilFU=");
    }

    @EventMapping
    public TextMessage
        handleTextMessageEvent(MessageEvent<TextMessageContent> event) throws IOException {
        LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));
        TextMessageContent content = event.getMessage();
        String textContext = content.getText();
        if (textContext.length() < 20) {
            return new TextMessage("Sorry your input is not valid"
                    + " the format should be /billboard tropical ArtistName");
        }
        String parser = textContext.substring(0, 19);
        String artist = textContext.substring(20, textContext.length());
        try {
            if (!parser.equals("/billboard tropical")) {
                throw new IllegalArgumentException();
            }
            String result = cekArtis(artist);
            if (result.equalsIgnoreCase("")) {
                return new TextMessage("Sorry, Artist " + artist + " is not in the chart");
            }
            return new TextMessage(result);
        } catch (IllegalArgumentException e) {
            return new TextMessage("Sorry, Artist " + textContext + " is not available");
        }
    }

    @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));
    }

    public static String cekArtis(String artis) throws IOException {
        Document doc = Jsoup.connect("https://www.billboard.com/charts/tropical-songs").get();
        Elements containers = doc.select(".chart-row__title");
        String hasil = "";
        for (int i = 0; i < 25; i++) {
            Element elements = containers.get(i);
            if (elements.select(".chart-row__artist").text().equalsIgnoreCase(artis)) {
                hasil += "\n" + elements.select(".chart-row__artist").text() + "\n"
                        + elements.select(".chart-row__song").text()
                        + "\n" + "Position : " + (i + 1) + "\n";
            }
        }
        return hasil;
    }
}
