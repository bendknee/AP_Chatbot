package JapanBillboard.controller;

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
public class JapanBillboardController {

    private static final Logger LOGGER = Logger.getLogger(JapanBillboardController.class.getName());

    static {
        System.setProperty("line.bot.channelSecret", "c30b82a49293"
                + "b7c16a9bf6488b7d3e63");
        System.setProperty("line.bot.channelToken", "g3S2UFypbYUxSPFjlgr0TA96yKG+R"
                + "ILbXbowKiis43NmW/285W84e7zAVPuW+L"
                + "8ZZuiPyakJNVmzouENCttynmsFPVkQZEM5zDUGbjdkCW0WCK8ISqtlF9vQ3"
                + "frGBsSbcR401NTPOiid0VFND71YhQdB04"
                + "t89/1O/w1cDnyilFU=");
    }

    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) throws IOException {
        LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));
        TextMessageContent content = event.getMessage();
        String textContext = content.getText();
        if (textContext.length() < 18) {
            return new TextMessage("Sorry your input is not valid");
        }
        String parser = textContext.substring(0, 19);
        String artist = textContext.substring(20, textContext.length());
        try {
            if (!parser.equals("/billboard japan100")) {
                throw new IllegalArgumentException();
            }
            String result = cekArtis(artist);
            if (result.equalsIgnoreCase("")) {
                return new TextMessage("Sorry, Artist " + artist + " is not in the chart");
            }
            return new TextMessage(result);
        } catch (IllegalArgumentException e) {
            return new TextMessage("Sorry, Artist" + textContext + " is not available");
        }
    }

    @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));
    }

    public static String cekArtis(String artis) throws IOException {
        Document doc = Jsoup.connect("https://www.billboard.com/charts/japan-hot-100").get();
        Elements containers = doc.select(".chart-row__title");
        String hasil = "";
        for (int i = 0; i < 100; i++) {
            Element elements = containers.get(i);
            if (elements.select(".chart-row__artist").text().equalsIgnoreCase(artis)) {
                hasil += "\n" + elements.select(".chart-row__artist").text() + "\n" +
                        elements.select(".chart-row__song").text() + "\n" + "Position : " + (i + 1) + "\n";
            }
        }
        return hasil;
    }
}
