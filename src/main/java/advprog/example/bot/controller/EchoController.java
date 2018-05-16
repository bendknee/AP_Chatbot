package advprog.example.bot.controller;

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
import java.util.ArrayList;
import java.util.logging.Logger;

import static java.lang.String.format;

@LineMessageHandler
public class EchoController {

    private static final Logger LOGGER = Logger.getLogger(EchoController.class.getName());

    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));
        TextMessageContent content = event.getMessage();
        String contentText = content.getText();
        
        String replyText = contentText.replace("/echo", "");
        return new TextMessage(replyText.substring(1));
    }

    @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));
    }

    @EventMapping
    private String scrapping(String mode, String date) throws IOException {
        String offset = "";
        ArrayList<String> val = new ArrayList<>();
        if (mode.toLowerCase().equals("daily")) {
            offset = "https://www.oricon.co.jp/rank/bd/d/"+date+"/";
        }
        else if (mode.toLowerCase().equals("weekly")) {
            offset = "https://www.oricon.co.jp/rank/bd/w/"+date+"/";
        }
        else {
            return "Input Mode Salah!!!\n\n\nFormat input\n/oricon bluray <weekly|daily> YYYY-MM-DD";
        }
        try {
            Document dc = Jsoup.connect(offset).timeout(3000).get();
            Elements body = dc.select("section.box-rank-entry");

            int i = 1;
            for (Element rank : body) {
                String title = rank.select("h2.title").text();
                String artist = rank.select("p.name").text();
                Elements list = rank.select("ul.list li");
                String release = list.get(0).text();
                release = release.substring(5, 9) + "-" + release.substring(10, 12) + "-" + release.substring(13, 15);
                val.add(format("(%d) %s - %s - %s", i, title, artist, release));
                i++;
            }

            String top10 = "";
            for (String e : val) {
                top10 = top10 + e + "\n";
            }
            return top10;
        }
        catch (IOException e) {
            return "Input tanggal salah atau tidak ditemukan!!!\n\n\nFormat input\n/oricon bluray <weekly|daily> YYYY-MM-DD";
        }
    }
}
