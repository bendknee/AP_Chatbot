package pasaranprimbon.bot.controller;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.logging.Logger;

@LineMessageHandler
public class PrimbonController {

    private static final Logger LOGGER =
            Logger.getLogger(PrimbonController.class.getName());

    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));
        TextMessageContent content = event.getMessage();
        String contentText = content.getText();

        String newContentText = contentText.replace("/primbon ", "");
        String replyText;

        System.out.println(contentText.substring(0,8));
        if (contentText.substring(0,8).equals("/primbon")) {
            try {
                String[] tanggalan = newContentText.split("-");
                LocalDate.of(Integer.parseInt(tanggalan[0]) - 2000, Integer.parseInt(tanggalan[1]),
                        Integer.parseInt(tanggalan[2]));

                int dayDifference = dayDifferenceGetter(newContentText);
                String dayName = dayGetter(dayDifference);
                String pasaranName = pasaranGetter(dayDifference);
                replyText = dayName + " " + pasaranName;
            } catch (Exception e) {
                replyText = "Please insert the correct date format (yyyy-MM-dd)";
            }
        } else {
            replyText = "Please use the format /primbon DATE";
        }

        return new TextMessage(replyText);
    }

    @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
            event.getTimestamp(), event.getSource()));
    }

    public int dayDifferenceGetter(String tanggal) {
        String[] tanggalan = tanggal.split("-");

        LocalDate referencedDate = LocalDate.of(-200, 01, 01);
        LocalDate givenDate = LocalDate.of(Integer.parseInt(tanggalan[0]) - 2000,
                Integer.parseInt(tanggalan[1]), Integer.parseInt(tanggalan[2]));

        long diff = ChronoUnit.DAYS.between(referencedDate, givenDate);
        int diffInt = (int) diff;

        return diffInt;
    }

    @SuppressWarnings("serial")
    public String dayGetter(int dayDifference) {
        ArrayList<String> dayList = new ArrayList<String>() {{
                add("Rabu");
                add("Kamis");
                add("Jumat");
                add("Sabtu");
                add("Minggu");
                add("Senin");
                add("Selasa");
            }
        };

        int day = dayDifference % 7;
        String dayName;

        if (day < 0) {
            dayName = dayList.get(dayList.size() + day);
        } else {
            dayName = dayList.get(day);
        }

        return dayName;
    }

    @SuppressWarnings("serial")
    public String pasaranGetter(int dayDifference) {
        ArrayList<String> pasaranList = new ArrayList<String>() {{
                add("Pon");
                add("Wage");
                add("Kliwon");
                add("Legi");
                add("Pahing");
            }
        };

        int pasaran = dayDifference % 5;
        String pasaranName;


        if (pasaran < 0) {
            pasaranName = pasaranList.get(pasaranList.size() + pasaran);
        } else {
            pasaranName = pasaranList.get(pasaran);
        }

        return pasaranName;
    }
}
