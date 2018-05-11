package advprog.example.bot.command;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SentimentCommand {

    public static String HELP_TEXT = "Usage: /sentiment [text]";

    // Changed every 7 days, Last modified: 11th May 2018
    private static String SUBSCRIPTION_KEY = "f85761fe86284fb085b9bce63a5c3738";

    public static Message execute(String args) {
        Optional<Double> optScore = getSentimentScore(args);
        if (!optScore.isPresent()) {
            return new TextMessage("An error occured while processing.");
        }

        double score = optScore.get();
        return new TextMessage(String.format("Sentiment: %.2f%%", score * 100.0));
    }

    private static Optional<Double> getSentimentScore(String text) {
        Optional<JsonNode> optJson = getSentimentJson(text);

        if (!optJson.isPresent()) {
            return Optional.empty();
        }

        JsonNode json = optJson.get();
        ArrayNode documents = (ArrayNode)json.get("documents");
        JsonNode document = documents.get(0);
        Double sentimentScore = document.get("score").asDouble();

        return Optional.of(sentimentScore);
    }

    private static Optional<JsonNode> getSentimentJson(String text) {
        String url = "https://westcentralus.api.cognitive.microsoft.com/text/analytics/v2.0/sentiment";

        Map<String,String> document = new HashMap<>();
        document.put("language", "en");
        document.put("id", "1");
        document.put("text", text);

        Map<String, Object> jsonContent = new HashMap<>();
        jsonContent.put("documents", Arrays.asList(document));
        String json;
        try {
            json = new ObjectMapper().writeValueAsString(jsonContent);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }


        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Ocp-Apim-Subscription-Key", SUBSCRIPTION_KEY);

            OutputStream os = con.getOutputStream();
            os.write(json.getBytes());
            os.flush();

            int responseCode = con.getResponseCode();
            if (responseCode != 200) {
                System.out.println("(ERROR) RESPONSE CODE: " + responseCode);
                return Optional.empty();
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    con.getInputStream()
            ));

            String output = br.readLine();

            con.disconnect();

            JsonNode jsonNode = new ObjectMapper().readTree(output);

            return Optional.of(jsonNode);

        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

}
