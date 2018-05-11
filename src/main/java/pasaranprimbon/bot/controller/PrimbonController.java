package pasaranprimbon.bot.controller;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.logging.Logger;

@LineMessageHandler
public class PrimbonController {

    private static final Logger LOGGER = Logger.getLogger(advprog.example.bot.controller.EchoController.class.getName());

    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));
        TextMessageContent content = event.getMessage();
        String contentText = content.getText();

        String newContentText = contentText.replace("/echo", "");
        int dayDifference = dayDifferenceGetter(newContentText);
        String dayName = dayGetter(dayDifference);
        String pasaranName = pasaranGetter(dayDifference);

        String replyText = ">" + dayName + " " + pasaranName;
        return new TextMessage(replyText.substring(1));
    }

    @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
            event.getTimestamp(), event.getSource()));
    }

    public int dayDifferenceGetter(String tanggal) {
        SimpleDateFormat reference = new SimpleDateFormat("yyyy-MM-dd");

        String[] tanggalan = tanggal.split("-");

        Date referencedDate = new Date(1800, 01, 01);
        Date givenDate = new Date(Integer.parseInt(tanggalan[0]), Integer.parseInt(tanggalan[1]), Integer.parseInt(tanggalan[2]));

        int diff = givenDate.getDate() - referencedDate.getDate();
        return diff;
    }

    public String dayGetter(int dayDifference) {
        ArrayList<String> dayList = new ArrayList<String>() {{
            add("Rabu");
            add("Kamis");
            add("Jumat");
            add("Sabtu");
            add("Minggu");
            add("Senin");
            add("Selasa");
        }};

        int day = dayDifference % 7;
        String dayName = dayList.get(day);

        return dayName;
    }

    public String pasaranGetter(int dayDifference) {
        ArrayList<String> pasaranList = new ArrayList<String>() {{
            add("Pon");
            add("Wage");
            add("Kliwon");
            add("Legi");
            add("Pahing");
        }};

        int pasaran = dayDifference % 5;
        String pasaranName = pasaranList.get(pasaran);

        return pasaranName;
    }
}