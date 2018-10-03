package advprog.flickr.bot.controller;

import com.linecorp.bot.model.action.URIAction;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.LocationMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.template.ImageCarouselColumn;
import com.linecorp.bot.model.message.template.ImageCarouselTemplate;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;


@LineMessageHandler
public class NearbyPhotosController {

    private static final Logger LOGGER = Logger.getLogger(NearbyPhotosController.class.getName());
    private final String baseUrl = "https://api.flickr.com/services/rest";
    private final String apiKey = "4847f0e678f60a5f7e213521c263deef";
    private final String apiMethod = "/?method=flickr.photos.search";
    private final String extensionParam = "&radius=2&per_page=5&format=json&nojsoncallback=1";

    @EventMapping
    public List<TextMessage> handleKeywordTrigger(MessageEvent<TextMessageContent> event) {
        LOGGER.fine(String.format("TextMessageContent(timestamp='%s',text='%s')",
                event.getTimestamp(), event.getMessage().getText()));

        if (event.getMessage().getText().toLowerCase().equals("nearby photos")) {
            TextMessage textMessage = new TextMessage("To use this bot, simply submit a "
                    + "location straightaway with the 'Share location' feature below.");
            return Collections.singletonList(textMessage);
        } else {
            return Collections.singletonList(null);
        }
    }

    @EventMapping
    public List<Message> handleLocationMessageEvent(MessageEvent<LocationMessageContent> event) {
        LOGGER.fine(String.format("LocationMessageContent(timestamp='%s',title='%s')",
                event.getTimestamp(), event.getMessage().getTitle()));

        LocationMessageContent content = event.getMessage();
        String latitude = Double.toString(content.getLatitude());
        String longitude = Double.toString(content.getLongitude());

        String flickrRestUrl =  baseUrl + apiMethod + "&api_key=" + apiKey + "&lat="
                + latitude + "&lon=" + longitude + extensionParam;

        String stringifiedJson = restGetMethod(flickrRestUrl);
        List<Map<String, String>> allPhotosData = jsonToMap(stringifiedJson);
        List<ImageCarouselColumn> carouselColumns = carouselColumnsGenerator(allPhotosData);
        if (carouselColumns.size() == 0) {
            String errorMessage = "No image found. Perhaps select a more populous location.";
            TextMessage textMessage = new TextMessage(errorMessage);
            return Collections.singletonList(textMessage);
        } else {
            TemplateMessage templateMessage = new TemplateMessage("Found " + carouselColumns.size()
                    + " images", new ImageCarouselTemplate(carouselColumns));
            return Collections.singletonList(templateMessage);
        }
    }

    private String restGetMethod(String url) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders header = new HttpHeaders();
        return restTemplate.getForObject(url, String.class, header);
    }

    private List<Map<String, String>> jsonToMap(String string) {
        try {
            List<Map<String, String>> allObjects = new ArrayList<>();
            JSONObject parsedJson = new JSONObject(string);
            JSONArray photos = parsedJson.getJSONObject("photos").getJSONArray("photo");

            for (int i = 0; i < photos.length(); ++i) {
                JSONObject singlePhoto = photos.getJSONObject(i);
                allObjects.add(customJsonAttributeGet(singlePhoto));
            }
            return allObjects;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<ImageCarouselColumn> carouselColumnsGenerator(
            List<Map<String, String>> allImages) {
        ArrayList<ImageCarouselColumn> carouselColumns = new ArrayList<>();
        for (Map<String, String> image : allImages) {
            String flickrSourceUrl = String.format(
                    "https://farm%s.staticflickr.com/%s/%s_%s.jpg", image.get("farm"),
                    image.get("server"), image.get("id"), image.get("secret"));
            String flickrWebUrl = String.format(
                    "https://www.flickr.com/photos/%s/%s", image.get("owner"), image.get("id")
            );
            String title;
            if (image.get("title").length() == 0) {
                title = "Untitled";
            } else if (image.get("title").length() > 12) {
                title = image.get("title").substring(0,9) + "...";
            } else {
                title = image.get("title");
            }
            carouselColumns.add(new ImageCarouselColumn(flickrSourceUrl,
                    new URIAction(title, flickrWebUrl)));
        }
        return carouselColumns;
    }

    private Map<String, String> customJsonAttributeGet(JSONObject jsonObject) {
        try {
            HashMap<String, String> objectMap = new HashMap<>();

            objectMap.put("id", jsonObject.getString("id"));
            objectMap.put("secret", jsonObject.getString("secret"));
            objectMap.put("server", jsonObject.getString("server"));
            objectMap.put("farm", jsonObject.getString("farm"));
            objectMap.put("title", jsonObject.getString("title"));
            objectMap.put("owner", jsonObject.getString("owner"));

            return objectMap;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
