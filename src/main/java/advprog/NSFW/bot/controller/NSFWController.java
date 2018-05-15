package advprog.NSFW.bot.controller;

import com.google.common.io.ByteStreams;
import com.linecorp.bot.client.MessageContentResponse;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.ImageMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.ImageMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Collections;
import java.util.UUID;
import java.util.logging.Logger;



@LineMessageHandler
public class NSFWController {

    private static final Logger LOGGER = Logger.getLogger(NSFWController.class.getName());

    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));
        TextMessageContent content = event.getMessage();
        try {
            String depan = content.getText().split(" ")[0];
            if (!depan.equalsIgnoreCase("/is_sfw")) {
                throw new IllegalArgumentException();
            }
            String url = content.getText().replace("/is_sfw ", "");
            String reply = checker(url);
            return new TextMessage(reply);
        }
        catch (IllegalArgumentException e){
            return new TextMessage("Command Not Found");
        } catch (IOException e) {
            return new TextMessage("Web Not Found");
        } catch (JSONException e) {
            return new TextMessage("Json Not Found");
        }
    }

    @EventMapping
    public TextMessage handleImageMessageEvent(MessageEvent<ImageMessageContent> event) throws IOException{
        String id = event.getMessage().getId();
        String url = "https://api.line.me/v2/bot/message/"+id+"/content";
        //auth(url);
        return new TextMessage(restGetMethod(url));
//        try {
//            String reply = checker(url);
//            return new TextMessage(reply);
//        }
//        catch (JSONException e) {
//            return new TextMessage("nsfw");
//        }
    }

    public static class DownloadedContent {
        Path path;
        String uri;
    }

    private static String createUri(String path) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(path).build()
                .toUriString();
    }

    public void auth(String url){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
    }

    private String restGetMethod(String url) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders header = new HttpHeaders();
        //HttpEntity<String> entity = new HttpEntity<>("parameters", header);
        return restTemplate.getForObject(url, String.class, header);
    }



    @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));
    }

    public String checker(String input) throws IOException, JSONException {
        String credentialsToEncode = "acc_0e29e261d8dc785" + ":" + "c9cb9ad5c23de1757e2d1c614e703939";
        String basicAuth = Base64.getEncoder().encodeToString(credentialsToEncode.getBytes(StandardCharsets.UTF_8));

        String endpoint_url = "https://api.imagga.com/v1/categorizations/nsfw_beta";
        //String image_url = "https://cdn.pornpics.com/pics1/2017-08-06/474907_16big.jpg";  //Porn
        //String image_url = "https://cdn.britannica.com/700x450/48/106048-120-A097ADC4.jpg"; //Sand Dunes
        String image_url = input;

        String url = endpoint_url + "?url=" + image_url;
        URL urlObject = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();

        connection.setRequestProperty("Authorization", "Basic " + basicAuth);

        int responseCode = connection.getResponseCode();

        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader connectionInput = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        String jsonResponse = connectionInput.readLine();

        connectionInput.close();

        return json(jsonResponse);
    }

    public String json(String inputjson) throws JSONException {

        JSONObject json = new JSONObject(inputjson);
        String hasil = json.getJSONArray("results").getJSONObject(0).getJSONArray("categories").getJSONObject(0).getString("name");
        if (hasil.equalsIgnoreCase("safe")) return "sfw";
        else {
            return "nsfw";
        }
    }
}