package advprog.example.bot.controller;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;


@LineMessageHandler
public class TextSimilarityController {

    private static final Logger LOGGER = Logger.getLogger(TextSimilarityController
            .class.getName());

    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));

        TextMessageContent content = event.getMessage();
        String contentText = content.getText();

        if (contentText.contains("/docs_sim") && contentText.length() > 10) {
            String url = generateUrl(contentText);
            String result = requestResult(url);
            System.out.println("RESULT: " + result);
            return new TextMessage(result);
        } else {
            return new TextMessage("Untuk membandingan 2 text dokumen, balas dengan "
                    + "/docs_sim 'TEXT1' 'TEXT2' atau /docs_sim URL1 URL2");
        }
    }

    @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));
    }

    public String generateUrl(String text) {
        System.out.println("CHAT: " + text);
        text = text.replace("/docs_sim ", "");
        String token = "08267f9bb04e40dc94f6181ddc9e56f4"; //NOTE! TOKEN CAN EXPIRED
        String url = "https://api.dandelion.eu/datatxt/sim/v1/?";

        if (text.contains("'")) {
            String[] arr = text.split("'");
            String text1 = arr[1].replace(" ", "%20");
            String text2 = arr[3].replace(" ", "%20");
            url += "text1=" + text1 + "&"
                    + "text2=" + text2 + "&"
                    + "token=" + token;
        } else {
            String[] arr = text.split(" ");
            String url1 = arr[0];
            String url2 = arr[1];
            url += "url1=" + url1 + "&"
                    + "url2=" + url2 + "&"
                    + "token=" + token;
        }

        System.out.println("URL: " + url);
        return url;
    }

    public String requestResult(String url) {
        try {
            URL urlObj = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            System.out.println("Response Code: " + connection.getResponseCode());

            BufferedReader input = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String inputLine = "";
            StringBuffer response = new StringBuffer();
            while ((inputLine = input.readLine()) != null) {
                response.append(inputLine);
            }
            input.close();

            System.out.println("JSON Response: " + response.toString());

            return getSimilarity(response.toString());

        } catch (Exception e) {
            return "ERROR! Untuk membandingan 2 text dokumen, balas dengan "
                    + "/docs_sim 'TEXT1' 'TEXT2' atau /docs_sim URL1 URL2";
        }
    }

    public String getSimilarity(String response) {
        if (response.contains("\"similarity\":1.0")) {
            return "100%";
        } else if (response.contains("\"similarity\":0.0")) {
            return "0%";
        } else {
            int index = response.indexOf("similarity") + 12;
            String similarity = response.substring(index, index + 6);
            return new BigDecimal(similarity).multiply(new BigDecimal(100)).toString()
                    .substring(0, 5) + "%";
        }

    }
}