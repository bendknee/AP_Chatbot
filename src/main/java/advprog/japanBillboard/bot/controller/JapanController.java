package advprog.japanBillboard.bot.controller;

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
import java.util.List;
import java.util.logging.Logger;

@LineMessageHandler
public class JapanController {

    private static final Logger LOGGER = Logger.getLogger(JapanController.class.getName());

    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));
        TextMessageContent content = event.getMessage();
        try {
            if (!content.getText().equalsIgnoreCase("/billboard japan100")) {
                throw new IllegalArgumentException();
            }
            String reply = japanScrapper();
            return new TextMessage(reply);
        }
        catch (IllegalArgumentException e){
            return new TextMessage("Command Not Found");
        } catch (IOException e) {
            return new TextMessage("Web Not Found");
        }
    }

    @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));
    }

    public String japanScrapper() throws IOException{
        Document dc = Jsoup.connect("https://www.billboard.com/charts/japan-hot-100").get();
        Elements body = dc.select("div.chart-row__main-display");
        StringBuilder sb = new StringBuilder();
        int i=0;

        for (Element step : body){
            String position = step.select(".chart-row__current-week").text();
            String method = step.select(".chart-row__song").text();
            String singer = step.select(".chart-row__artist").text();

            sb.append("(").append(position).append(")").append(" ").append(method)
                    .append(" - ").append(singer).append("\n");
            i++;
            if (i==10){
                break;
            }
        }
        return sb.toString();
    }
}
