package advprog.example.bot.controller;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.action.MessageAction;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.LocationMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.template.CarouselColumn;
import com.linecorp.bot.model.message.template.CarouselTemplate;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;

@LineMessageHandler
public class UberBotController {
    @Autowired
    private LineMessagingClient lineMessagingClient;

    private LocationMessageContent locationMessageContent;
    private String location;

    private static final int STATE_GENERAL = 0;
    private static final int STATE_ADD_LOCATION = 1;
    private static final int STATE_DESTINATION = 2;
    private static final int STATE_CONFIRMATION = 3;
    private static final int STATE_NAME_LOCATION = 4;
    private static final int STATE_DELETE_LOCATION = 5;

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
            if (contentText.equals("/uber")) {
                replyMessage = uberCommand();
            } else if (contentText.equals("/add_destination")) {
                if (databaseSize() < 10) {
                    state = STATE_ADD_LOCATION;
                    replyMessage = "Perintah /add_destination diterima, "
                            + "silahkan kirim lokasi anda";
                    reply(replyToken, new TextMessage(replyMessage));
                } else {
                    replyMessage = "Banyak lokasi maksimal 10, silahkan hapus lokasi "
                            + "dari database\n\n jalankan perintah /remove_destination";
                    reply(replyToken, new TextMessage(replyMessage));
                }
            } else if (contentText.equals("/remove_destination")) {
                if (!dataIsEmpty()) {
                    state = STATE_DELETE_LOCATION;
                    replyMessage = "Perintah /remove_destination diterima, silahkan pilih "
                            + "lokasi yang ingin dihapus";
                    TextMessage textMessage = new TextMessage(replyMessage);
                    TemplateMessage templateMessage =
                            new TemplateMessage("Choose Destination", getCarouselTemplateMessage());
                    reply(replyToken, Arrays.asList(textMessage, templateMessage));
                } else {
                    replyMessage = "Data destination kosong! Silahkan jalankan "
                            + "command /add_destination";
                    reply(replyToken, new TextMessage(replyMessage));
                }

            } else {
                replyMessage = "Command uber-bot-ps:\n/uber\nBot calculates the distance"
                        + " between current user location and destination\n\n"
                        + "/add_destination\nBot saves pair of name and its associated "
                        + "location\n\n"
                        + "/remove_destination\nUser chooses a destination to remove "
                        + "from the bot";
                reply(replyToken, new TextMessage(replyMessage));
            }
        } else if (state == STATE_NAME_LOCATION) {
            if (nameIsUnique(contentText)) {
                addDestination(contentText);
                state = STATE_GENERAL;
                replyMessage = "Lokasi telah berhasil disimpan";
                reply(replyToken, new TextMessage(replyMessage));
            } else {
                replyMessage = "Nama sudah ada di data, silahkan masukan nama lain";
                reply(replyToken, new TextMessage(replyMessage));
            }
        } else if (state == STATE_DELETE_LOCATION) {
            if (!nameIsUnique(contentText)) {
                location = contentText;
                state = STATE_CONFIRMATION;
                replyMessage = "Apakah anda yakin ingin menghapus lokasi? (yes/no)";
                reply(replyToken, new TextMessage(replyMessage));
            } else {
                replyMessage = "Nama lokasi tidak ada di database, silahkan pilih lokasi kembali";
                TextMessage textMessage = new TextMessage(replyMessage);
                TemplateMessage templateMessage =
                        new TemplateMessage("Choose Destination", getCarouselTemplateMessage());
                reply(replyToken, Arrays.asList(textMessage, templateMessage));
            }
        } else if (state == STATE_CONFIRMATION) {
            if (contentText.equalsIgnoreCase("yes")) {
                state = STATE_GENERAL;
                deleteDestination(location);
                replyMessage = "Lokasi telah dihapus";
                reply(replyToken, new TextMessage(replyMessage));
            } else if (contentText.equalsIgnoreCase("no")) {
                state = STATE_GENERAL;
                replyMessage = "Lokasi tidak dihapus";
                reply(replyToken, new TextMessage(replyMessage));
            } else {
                replyMessage = "Apakah anda yakin ingin menghapus lokasi? (yes/no)";
                reply(replyToken, new TextMessage(replyMessage));
            }
        } else if (state == STATE_ADD_LOCATION) {
            replyMessage = "Silahkan kirim lokasi anda";
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
            replyMessage = "Lokasi diterima, silahkan beri nama lokasi tersebut "
                    + "(Contoh: Wisma Rossela)";
            reply(replyToken, new TextMessage(replyMessage));
        }

        return replyMessage;
    }

    private void reply(String replyToken, Message message) {
        lineMessagingClient.replyMessage(new ReplyMessage(replyToken, Arrays.asList(message)));
    }

    private void reply(String replyToken, List<Message> message) {
        lineMessagingClient.replyMessage(new ReplyMessage(replyToken, message));
    }

    @SuppressWarnings("unchecked")
    private void addDestination(String name) throws Exception {
        JSONObject data = new JSONObject();
        data.put("nama", name);
        JSONObject lokasi = new JSONObject();
        lokasi.put("latitude", locationMessageContent.getLatitude());
        lokasi.put("longitude", locationMessageContent.getLongitude());
        data.put("lokasi", lokasi);

        JSONObject json = getData();
        JSONArray arr = (JSONArray) json.get("data");
        arr.add(data);
        json.put("data", arr);

        FileWriter file = new FileWriter("data.json");
        file.write(json.toJSONString());
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
            JSONObject json = new JSONObject();
            JSONArray arr = new JSONArray();
            arr.add(data);
            json.put("data", arr);

            FileWriter file = new FileWriter("data.json");
            file.write(json.toJSONString());
            file.close();

            return getData();
        }
    }

    private CarouselTemplate getCarouselTemplateMessage() throws Exception {
        JSONArray arr = (JSONArray) getData().get("data");
        ArrayList<CarouselColumn> columns = new ArrayList<CarouselColumn>();

        for (int i = 0; i < arr.size(); i++) {
            JSONObject data = (JSONObject) arr.get(i);
            String nama = (String) data.get("nama");
            JSONObject lokasi = (JSONObject) data.get("lokasi");
            String latitude = Double.toString((Double) lokasi.get("latitude"));
            String longitude = Double.toString((Double) lokasi.get("longitude"));

            MessageAction action = new MessageAction("Pilih " + nama, nama);
            CarouselColumn column = new CarouselColumn(
                    "https://cdn3.iconfinder.com/data/icons/map-pins-v-2/512/map_pin_"
                            + "arrow_destination_four_points-256.png",
                    nama,
                    "Latitude: " + latitude + " | " + "Longitude: " + longitude,
                    Arrays.asList(action)
            );
            columns.add(column);
        }
        return new CarouselTemplate(columns);
    }

    @SuppressWarnings("unchecked")
    private void deleteDestination(String nama) throws Exception {
        JSONObject json = getData();
        JSONArray arr = (JSONArray) json.get("data");
        int index = 0;

        for (int i = 0; i < arr.size(); i++) {
            JSONObject data = (JSONObject) arr.get(i);
            
            if (((String) data.get("nama")).equals(nama)) {
                index = i;
                break;
            }
        }

        arr.remove(index);
        json.put("data", arr);

        FileWriter file = new FileWriter("data.json");
        file.write(json.toJSONString());
        file.close();
    }

    private boolean dataIsEmpty() throws Exception {
        return ((JSONArray) getData().get("data")).isEmpty();
    }

    private boolean nameIsUnique(String name) throws Exception {
        JSONArray arr = (JSONArray) getData().get("data");

        for (int i = 0; i < arr.size(); i++) {
            JSONObject obj = (JSONObject) arr.get(i);
            if (name.equalsIgnoreCase((String) obj.get("nama"))) {
                return false;
            }
        }

        return true;
    }

    private int databaseSize() throws  Exception {
        return ((JSONArray) getData().get("data")).size();
    }

    private String uberCommand() {
        return "";
    }

    @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));
    }
}
