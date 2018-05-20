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

}
