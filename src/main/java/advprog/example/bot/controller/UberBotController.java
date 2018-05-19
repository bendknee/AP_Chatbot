package advprog.example.bot.controller;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.LocationMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Logger;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;

@LineMessageHandler
public class UberBotController {
    @Autowired
    private LineMessagingClient lineMessagingClient;

    private LocationMessageContent locationMessageContent;

    private final static int STATE_GENERAL = 0;
    private final static int STATE_ADD_LOCATION = 1;
    private final static int STATE_DESTINATION = 2;
    private final static int STATE_CONFIRMATION = 3;
    private final static int STATE_NAME_LOCATION = 4;
    private final static int STATE_DELETE_LOCATION = 1;

    private static int state = STATE_GENERAL;

    private static final Logger LOGGER = Logger.getLogger(UberBotController.class.getName());
    @EventMapping
    public String handleTextMessageEvent(MessageEvent<TextMessageContent> event) throws Exception {
        LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));

        TextMessageContent content = event.getMessage();
        String contentText = content.getText();
        String replyToken = event.getReplyToken();
        String replyMessage = "";

        if (state == STATE_GENERAL) {
            if (contentText == "/uber") {
                replyMessage = uberCommand();
            } else if (contentText == "/add_destination") {
                state = STATE_ADD_LOCATION;
                replyMessage = "Perintah /add_destination diterima, silahkan kirim lokasi anda";
                reply(replyToken, new TextMessage(replyMessage));
            } else if (contentText == "/remove_destination") {
                replyMessage = removeDestinationCommand();
            }
        } else if (state == STATE_NAME_LOCATION) {
            addDestination(contentText);
            state = STATE_GENERAL;
            replyMessage = "Lokasi telah berhasil disimpan";
            reply(replyToken, new TextMessage(replyMessage));
        }

        return replyMessage;
    }

    @EventMapping
    public String handleLocationMessageEvent(MessageEvent<LocationMessageContent> event) {
        String replyToken = event.getReplyToken();
        String replyMessage = "";

        if (state == STATE_ADD_LOCATION) {
            locationMessageContent = event.getMessage();
            state = STATE_NAME_LOCATION;
            replyMessage = "Lokasi diterima, silahkan beri nama lokasi tersebut (Contoh: Wisma Rossela)";
            reply(replyToken, new TextMessage(replyMessage));
        }

        return replyMessage;
    }

    private void reply(String replyToken, Message message) {
        lineMessagingClient.replyMessage(new ReplyMessage(replyToken, Arrays.asList(message)));
    }

    @SuppressWarnings("unchecked")
    private void addDestination(String name) throws Exception {
        JSONObject data = getData();
        data.put("nama", name);
        JSONObject lokasi = new JSONObject();
        lokasi.put("latitude", locationMessageContent.getLatitude());
        lokasi.put("longitude", locationMessageContent.getLongitude());
        data.put("lokasi", lokasi);

        FileWriter file = new FileWriter("data.json");
        file.write(data.toJSONString());
        file.close();
    }

    @SuppressWarnings("unchecked")
    private JSONObject getData() throws Exception {
        try {
            return (JSONObject) new JSONParser().parse(new FileReader("data.json"));
        } catch (IOException e) {
            JSONObject data = new JSONObject();
            data.put("nama", "Wisma Rossela");
            JSONObject lokasi = new JSONObject();
            lokasi.put("latitude", -6.362413);
            lokasi.put("longitude", 106.818845);
            data.put("lokasi", lokasi);

            FileWriter file = new FileWriter("data.json");
            file.write(data.toJSONString());
            file.close();

            return getData();
        }
    }

    private String uberCommand() {
      return "";
    }

    private String removeDestinationCommand() {
      return "";
    }

    @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));
    }
}
