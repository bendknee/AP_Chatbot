package advprog.nsfw.bot.controller;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.ImageMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;


@LineMessageHandler
public class NsfwController {

    private static final Logger LOGGER = Logger.getLogger(NsfwController.class.getName());

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
        } catch (IllegalArgumentException e){
            return new TextMessage("Inputan tidak tersedia nih, coba /is_sfw atau masukan gambar");
        } catch (IOException e) {
            return new TextMessage("Web Not Found");
        } catch (JSONException e) {
            return new TextMessage("Link yang kamu masukkan tidak benar, masukkan link yang benar ya :)");
        }
    }

    @EventMapping
    public TextMessage handleImageMessageEvent(
            MessageEvent<ImageMessageContent> event) throws IOException {
        String id = event.getMessage().getId();
        String url = "https://api.line.me/v2/bot/message/" + id + "/content";
        try {
            String reply = checker(url);
            return new TextMessage(reply);
        }
        catch (JSONException e) {
            return new TextMessage("nsfw");
        }
    }

//    static String getBasicAuth(String username, String password) {
//        String auth = username + ":" + password;
//        String auth64 = new String(org.apache.tomcat.util.codec.binary.Base64.encodeBase64(auth.getBytes()));
//
//        return "Basic " + auth64;
//    }
//
//    public String nembakApi(String id) {
//        String basic = getBasicAuth("uname", "password");
//        RestTemplate restTemplate = new RestTemplate();
//        String url = "https://api.line.me/v2/bot/message/"+id+"/content";
//
//        UriComponentsBuilder uri = UriComponentsBuilder
//                .fromUriString("https://api.imagga.com/v1/colors")
//                .queryParam("url", url);
//        LOGGER.info(uri.toUriString());
//
//        HttpHeaders header = new HttpHeaders();
//        header.add("Authorization", basic);
//
//        HttpEntity<String> request = new HttpEntity<String>(String.valueOf(header));
//        ResponseEntity<String> response = restTemplate.exchange(uri.toUriString(), HttpMethod.GET, request, String.class);
//
//        LOGGER.info(response.getBody());
//        return response.getBody();
//        //jsonnode
//    }
//
//    public void auth(String url){
//        RestTemplate restTemplate = new RestTemplate();
//        HttpHeaders headers = new HttpHeaders();
//        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
//
//        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
//
//        //return restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
//        restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
//    }
//
//    private String restGetMethod(String url) {
//        String basic = getBasicAuth("uname", "password");
//        RestTemplate restTemplate = new RestTemplate();
//        HttpHeaders header = new HttpHeaders();
//        header.add("Authorization", basic);
//        //HttpEntity<String> entity = new HttpEntity<>("parameters", header);
//        return restTemplate.getForObject(url, String.class, header);
//    }

    @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));
    }

    public String checker(String input) throws IOException, JSONException {
        String credentialsToEncode = "acc_0e29e261d8dc785" + ":"
                + "c9cb9ad5c23de1757e2d1c614e703939";
        String basicAuth = java.util.Base64.getEncoder().encodeToString(
                credentialsToEncode.getBytes(StandardCharsets.UTF_8));

        String endpointUrl = "https://api.imagga.com/v1/categorizations/nsfw_beta";
        //String image_url = "https://cdn.pornpics.com/pics1/2017-08-06/474907_16big.jpg";  //Porn
        String imageUrl = input;

        String url = endpointUrl + "?url=" + imageUrl;
        URL urlObject = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();

        connection.setRequestProperty("Authorization", "Basic " + basicAuth);

        int responseCode = connection.getResponseCode();

        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader connectionInput = new BufferedReader(
                new InputStreamReader(connection.getInputStream()));

        String jsonResponse = connectionInput.readLine();

        connectionInput.close();

        return json(jsonResponse);
    }

    public String json(String inputjson) throws JSONException {

        JSONObject json = new JSONObject(inputjson);
        String hasil = json.getJSONArray("results").getJSONObject(0).getJSONArray("categories")
                .getJSONObject(0).getString("name");
        if(hasil.equalsIgnoreCase("safe")) {
            return "sfw";
        } else {
            return "nsfw";
        }
    }
}