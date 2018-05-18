package botnewrelease.controller;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import com.ritaja.xchangerate.api.CurrencyConverter;
import com.ritaja.xchangerate.api.CurrencyConverterBuilder;
import com.ritaja.xchangerate.api.CurrencyNotSupportedException;
import com.ritaja.xchangerate.endpoint.EndpointException;
import com.ritaja.xchangerate.service.ServiceException;
import com.ritaja.xchangerate.storage.StorageException;
import com.ritaja.xchangerate.util.Currency;
import com.ritaja.xchangerate.util.Strategy;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.SocketTimeoutException;
import java.util.logging.Logger;

import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
    private static final String API_KEY = "518f742dc253a41c314750f3ad70c03b";

    @EventMapping
    public TextMessage
        handleTextMessageEvent(MessageEvent<TextMessageContent> event)
            throws IOException, JSONException, CurrencyNotSupportedException,
            ServiceException, EndpointException, StorageException {
        LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));
        TextMessageContent content = event.getMessage();
        String contentText = content.getText();
        if (contentText.length() < 21) {
            return new TextMessage("Sorry your input is not valid "
                    + "the format should be /vgmdb OST this month");
        }
        String parser = contentText.substring(0, 20);
        try {
            if (!parser.equalsIgnoreCase("/vgmdb OST this month")) {
                throw new IllegalArgumentException();
            }
            String result = cekNewRelease();
            return new TextMessage(result.substring(0));

        } catch (IllegalArgumentException e) {
            return new TextMessage("Sorry your input is not valid "
                    + "the format should be /vgmdb OST this month");
        } catch (SocketTimeoutException e) {
            return new TextMessage("Sorry there is a commection "
                    + "timeout please try again");
        }
    }

    @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));
    }

    public static String cekNewRelease()
            throws IOException, JSONException, CurrencyNotSupportedException,
            ServiceException, EndpointException, StorageException {
        String hasil = "";
        Document doc = Jsoup.connect("https://vgmdb.net/db/calendar.php?year=2018&month=5").get();
        Elements containers = doc.getElementsByClass("album_infobit_detail");
        for (Element element : containers) {
            String title = element.select("li > a.albumtitle.album-game").attr("title");
            String[] value = element.child(1).text().split(" | ");
            if (title.toLowerCase().contains("original")
                    && title.toLowerCase().contains("soundtrack")) {
                int realPrice = convertHarga(value[2], value[3]).intValueExact();
                hasil += (title + " : " + realPrice + " IDR" + "\n");
            }
        }
        return hasil;
    }

    public static BigDecimal convertHarga(String price, String typeMoney)
            throws JSONException, CurrencyNotSupportedException,
            ServiceException, EndpointException, StorageException {
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
