package botnewrelease.controller;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.SocketException;
import java.util.logging.Logger;

@LineMessageHandler
public class NewReleaseController {

    static {
        System.setProperty("line.bot.channelSecret", "6d36e276040"
                + "357906dc55e6910be12fc");
        System.setProperty("line.bot.channelToken", "QpMXcgUuMTdYJ1SkPA6JOH5NmAC"
                + "87qulDl/RM5haiibS7pBq69Za1Z7RkWzLERjTTOz0c4FTK2eiMKbabg/0"
                + "1pwRv6HSxWOcbfmfZEzqIh+7V7Tq7bOFXk/MA/cQPBp0C5sSY"
                + "fjnFKyHnL9B155ogwdB04t89/1O/w1cDnyilFU=");
    }

    private static final Logger LOGGER = Logger.getLogger(NewReleaseController.class.getName());

    public static void main(String[] args) throws IOException {
        cekNewRelease();
    }

    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) throws IOException {
        LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));
        TextMessageContent content = event.getMessage();
        String contentText = content.getText();
        /* To do method process (Stub)*/
        if (contentText.length() < 22) {
            return new TextMessage("Sorry your input is not valid "
                    + "the format should be /vgmdb OST this month");
        }
        String parser = contentText.substring(0, 21);
        try {
            if (!parser.equalsIgnoreCase("/vgmdb OST this month")) {
                throw new IllegalArgumentException();
            }
            String result = cekNewRelease();
            return new TextMessage(result.substring(0));

        } catch (IllegalArgumentException e) {
            return new TextMessage("Sorry your input is not valid "
                    + "the format should be /vgmdb OST this month");
        }
    }

    @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));
    }

    // To do method
    public static String cekNewRelease() throws IOException {
        String hasil = "";
        Document doc = Jsoup.connect("https://vgmdb.net/db/calendar.php?year=2018&month=5").get();
        Elements containers = doc.getElementsByClass("album_infobit_detail");
        for (Element element : containers) {
            String title = element.select("li > a.albumtitle.album-game").attr("title");
            String value[] = element.child(1).text().split(" | ");
            if (title.toLowerCase().contains("original") && title.toLowerCase().contains("soundtrack")) {
                convertHarga(value[2], value[3]);
            }
        }
        return hasil;
    }

    public static double convertHarga(String price, String typeMoney) {
        int harga = 0;

        return 0.0;
    }
}
