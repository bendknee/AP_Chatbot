package botnewrelease.controller;

import com.linecorp.bot.client.LineMessagingServiceBuilder;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.response.BotApiResponse;
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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import retrofit2.Response;

@LineMessageHandler
public class NewReleaseController {

    static {
        System.setProperty("line.bot.channelSecret", "6aaea5f3be3aff04"
                + "b50ff183727493a3");
        System.setProperty("line.bot.channelToken", "zoSKZdAdyRLBZ5TMpmVf5VR/j0AVVaax1a"
                + "HLnXoAvnwvB1zzVWmcHdHIQ/Hm1wmg55KuC1EOEqggMIOcuo2DNP8JL1tw"
                + "3wh7kIl8R2gAOiKLTdVb7oLUHRDWSrKZo51y8EUrV+nDn1aF0ehWVM"
                + "jw0AdB04t89/1O/w1cDnyilFU=");
    }

    private static final Logger LOGGER = Logger.getLogger(NewReleaseController.class.getName());
    private static final String API_KEY = "518f742dc253a41c314750f3ad70c03b";

    public static void main(String[] args) throws IOException, JSONException, CurrencyNotSupportedException,
            ServiceException, EndpointException, StorageException {
        String result = cekNewRelease();
        System.out.println(result);
    }

    @EventMapping
    public static List<Message>
        handleTextMessageEvent(MessageEvent<TextMessageContent> event)
            throws JSONException, IOException, CurrencyNotSupportedException,
            ServiceException, EndpointException, StorageException {
        LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));
        TextMessageContent content = event.getMessage();
        String contentText = content.getText();
        if (contentText.length() < 21) {
            //return new TextMessage("The format should be /vgmdb OST this month");
        }
        String parser = contentText.substring(0, 21);
        try {
            if (!parser.equalsIgnoreCase("/vgmdb OST this month") ||
                    contentText.length() > 22) {
                throw new IllegalArgumentException();
            }
            /*ReplyMessage messages = Arrays.asList(new TextMessage(cekNewRelease()), new TextMessage(""));
            Response<BotApiResponse> respon = LineMessagingServiceBuilder
                    .create("zoSKZdAdyRLBZ5TMpmVf5VR/j0AVVaax1a"
                    + "HLnXoAvnwvB1zzVWmcHdHIQ/Hm1wmg55KuC1EOEqggMIOcuo2DNP8JL1tw"
                    + "3wh7kIl8R2gAOiKLTdVb7oLUHRDWSrKZo51y8EUrV+nDn1aF0ehWVM"
                    + "jw0AdB04t89/1O/w1cDnyilFU=")
                    .build()
                    .replyMessage(messages)
                    .execute();*/
            String hasil = cekNewRelease();
            List<Message> mess = new ArrayList<>();
            mess.add(new TextMessage(hasil.substring(0,1996)));
            mess.add(new TextMessage(hasil.substring(1997,2036)));
            //return new TextMessage(cekNewRelease().substring(0, 1996));
            return mess;
        } catch (IllegalArgumentException e) {
            /*return new TextMessage("Sorry, your input is not valid it should be"
                    + "/vgmdb OST this month");*/
        }
        return null;
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
                int realPrice = convertHarga(value[2], value[3]);
                hasil += (title + " : " + realPrice + " IDR" + "\n");
            }
        }
        return hasil;
    }

    public static int convertHarga(String price, String typeMoney)
            throws JSONException, CurrencyNotSupportedException,
            ServiceException, EndpointException, StorageException {
        CurrencyConverter converter = new CurrencyConverterBuilder()
                .strategy(Strategy.CURRENCY_LAYER_FILESTORE)
                .accessKey(API_KEY)
                .buildConverter();

        converter.setRefreshRateSeconds(100000);
        if (typeMoney.equalsIgnoreCase("JPY")) {
            return converter.convertCurrency(new BigDecimal(price),
                    Currency.JPY, Currency.IDR).intValueExact();
        } else if (typeMoney.equalsIgnoreCase("USD")) {
            return converter.convertCurrency(new BigDecimal(price),
                    Currency.USD, Currency.IDR).intValueExact();
        } else if (typeMoney.equalsIgnoreCase("EUR")) {
            return converter.convertCurrency(new BigDecimal(price),
                    Currency.EUR, Currency.IDR).intValueExact();
        } else if (typeMoney.equalsIgnoreCase("GBP")) {
            return converter.convertCurrency(new BigDecimal(price),
                    Currency.GBP, Currency.IDR).intValueExact();
        } else if (typeMoney.equalsIgnoreCase("TWD")) {
            return converter.convertCurrency(new BigDecimal(price),
                    Currency.TWD, Currency.IDR).intValueExact();
        } else {
            return 0;
        }
    }
}
