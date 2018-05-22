package advprog.fakenews.bot.controller;

import advprog.fakenews.bot.BotFakeNewsApplication;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.source.GroupSource;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

@LineMessageHandler
public class FakeNewsController {
    BotFakeNewsApplication x = new BotFakeNewsApplication();

    private static final Logger LOGGER = Logger.getLogger(FakeNewsController.class.getName());

    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));
        TextMessageContent content = event.getMessage();
        List<String> input = new ArrayList<>(Arrays.asList("/is_fake", "/is_satire", "/is_conspiracy", "/add_filter"));
        try {
            String depan = content.getText().split(" ")[0];
            if (depan.equalsIgnoreCase("/is_fake")) {
            }
            //throw new IllegalArgumentException();
            if (event.getSource()instanceof GroupSource);
            String url = content.getText().replace(depan, "");
            String reply = "hh";
            return new TextMessage(reply);
        } catch (IllegalArgumentException e){
            return new TextMessage("Inputan tidak tersedia nih, coba /is_sfw atau masukan gambar");
        }
    }

    @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));
    }

    public void newsChecker(){
        return;
    }

    public void storeData(){
        return;
    }

}
