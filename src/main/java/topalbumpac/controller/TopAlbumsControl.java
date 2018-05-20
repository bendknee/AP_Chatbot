package topalbumpac.controller;

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
public class TopAlbumsControl {

    static {
        System.setProperty("line.bot.channelSecret", "9e514c1bbfd82d"
                + "65c9c62738e335ad0c");
        System.setProperty("line.bot.channelToken", "ZBfd4J0OQJsmM96kLQv8lJ"
                + "Z6HROSPl082xGLEE1Mhmc5gofdxygtkYDkErflbC4hi89cYnBU"
                + "L139T8yemI0yjX9J5LeWsWf1LDJviTYReTkbl15Pu1Kje9"
                + "wBce9VP1hEtIoARacjxCQJu9hRj5iy3wd"
                + "B04t89/1O/w1cDnyilFU=");
    }

    private static final Logger LOGGER = Logger.getLogger(TopAlbumsControl.class.getName());
    private static final String API_KEY = "518f742dc253a41c314750f3ad70c03b";

    /*public static void main(String[] args) throws IOException, JSONException,
            CurrencyNotSupportedException,
            ServiceException, EndpointException, StorageException {
        String hasil = cektop20();
        System.out.println(hasil);
    }*/

    @EventMapping
    public TextMessage
        handleTextMessageEvent(MessageEvent<TextMessageContent> event)
            throws IOException, JSONException, CurrencyNotSupportedException,
            ServiceException, EndpointException, StorageException {
        LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));
        TextMessageContent content = event.getMessage();
        String contentText = content.getText();
        if (contentText.length() < 19) {
            return new TextMessage("Sorry your input is not valid "
                    + "the format should be /vgmdb most_popular");
        }
        String parser = contentText.substring(0, 19);
        try {
            if (!parser.equalsIgnoreCase("/vgmdb most_popular") ||
                    contentText.length() > 20) {
                throw new IllegalArgumentException();
            }
            String result = cektop20();
            return new TextMessage(result.substring(0));

        } catch (IllegalArgumentException e) {
            return new TextMessage("Sorry your input is not valid "
                    + "the format should be /vgmdb most_popular");
        } catch (SocketTimeoutException e) {
            return new TextMessage("Sorry there is timeout cause vgmdb.net "
                    + "is slow, please try again later");
        }
    }

    @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));
    }

    public static String cektop20() throws IOException, JSONException, CurrencyNotSupportedException,
            ServiceException, EndpointException, StorageException {
        String hasil = "";
        try {
            int i = 0;
            int ptrcount = 0;
            Document doc = Jsoup.connect("https://vgmdb.net/db/statistics.php?do=top_rated").get();
            Elements containers = doc.select("span.albumtitle[lang=\"en\"]");
            int id = 14;
            int hrefl = 22;
            int[] pointer = new int[]
                    {22, 24, 20, 23, 22, 26, 21, 16, 23, 19, 20, 21, 21, 21, 19, 23, 20, 19, 22, 18};
            for (Element element : containers) {
                String judul = element.text();
                String rating = doc.select("td").get(id).text().substring(0, 4);
                String url = doc.select("a[href]").get(hrefl).attr("href");
                Document link = Jsoup.connect(url).get();
                Element elem = link.select("td").get(pointer[ptrcount]);
                //Elements elem = link.select("table").select("tr");
                //String value = getHarga(elem);
                //System.out.println(value);
                String temp = elem.text();
                if (temp.equalsIgnoreCase("Not for sale")) {
                    hasil += ((i + 1) + " - " + judul + " - " + rating + " (Not for Sale)" + "\n");
                } else {
                    String[] harga = temp.split(" ");
                    int value = convertHarga(harga[0], harga[1]).intValueExact();
                    hasil += ((i + 1) + " - " + judul + " - " + rating + " (" + value + " IDR)" + "\n");
                }
                i++;
                ptrcount++;
                id += 4;
                hrefl++;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return hasil += "Sorry, the rest are not available because "
                    + "there are some mistakes please contact the admin (Winston Chandra).";
        }
        return hasil;
    }

    /*public static String getHarga(Elements elem) {
        String result = "";
        for (Element table : elem) {
            Elements tdss = table.getElementsByTag("td");
            Elements tds = tdss.next();
            result = tds.text();
            return result;
        }
        return result;
    }*/

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
