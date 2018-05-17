package top20albums.controller;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.logging.Logger;

@LineMessageHandler
public class top20Contoller {

    static {
        System.setProperty("line.bot.channelSecret", "6d36e276040"
                + "357906dc55e6910be12fc");
        System.setProperty("line.bot.channelToken", "QpMXcgUuMTdYJ1SkPA6JOH5NmAC"
                + "87qulDl/RM5haiibS7pBq69Za1Z7RkWzLERjTTOz0c4FTK2eiMKbabg/0"
                + "1pwRv6HSxWOcbfmfZEzqIh+7V7Tq7bOFXk/MA/cQPBp0C5sSY"
                + "fjnFKyHnL9B155ogwdB04t89/1O/w1cDnyilFU=");
    }

    private static final Logger LOGGER = Logger.getLogger(top20Contoller.class.getName());
    private static final String API_KEY = "518f742dc253a41c314750f3ad70c03b";

    @EventMapping
    public TextMessage
        handleTextMessageEvent(MessageEvent<TextMessageContent> event)
            throws IOException, JSONException {
        LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));
        TextMessageContent content = event.getMessage();
        String contentText = content.getText();
        /* To do method process (Stub)*/
        if (contentText.length() < 19) {
            return new TextMessage("Sorry your input is not valid "
                    + "the format should be /vgmdb OST this month");
        }
        String parser = contentText.substring(0, 18);
        try {
            if (!parser.equalsIgnoreCase("/vgmdb OST this month")) {
                throw new IllegalArgumentException();
            }
            String result = cektop20();
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
    public static String cektop20() throws IOException, JSONException {
        String hasil = "";
        Document doc = Jsoup.connect("https://vgmdb.net/db/statistics.php?do=top_rated").get();
        Elements containers = doc.getElementsByClass("");
        for (Element element : containers) {

        }
        return hasil;
    }

    public static BigDecimal convertHarga(String price, String typeMoney) throws JSONException {
        CurrencyConverter converter = new CurrencyConverterBuilder()
                .strategy(Strategy.CURRENCY_LAYER_FILESTORE)
                .accessKey(API_KEY)
                .buildConverter();

        converter.setRefreshRateSeconds(100000);
        if (typeMoney.equalsIgnoreCase("JPY")) {
            return converter.convertCurrency(new BigDecimal(price), Currency.JPY, Currency.IDR);
        } else if (typeMoney.equalsIgnoreCase("USD")) {
            return converter.convertCurrency(new BigDecimal(price), Currency.USD, Currency.IDR);
        } else if (typeMoney.equalsIgnoreCase("EUR")) {
            return converter.convertCurrency(new BigDecimal(price), Currency.EUR, Currency.IDR);
        } else if (typeMoney.equalsIgnoreCase("GBP")) {
            return converter.convertCurrency(new BigDecimal(price), Currency.GBP, Currency.IDR);
        } else {
            return null;
        }
    }
}
