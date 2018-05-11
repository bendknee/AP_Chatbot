package advprog.example.bot.command;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.linecorp.bot.model.message.ImageMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Optional;


public class XkcdCommand {

    public static String HELP_TEXT = "Usage: /xkcd [id]";

    public static Message execute(String args) {

        int id;
        try {
            id = Integer.parseInt(args);
        } catch (NumberFormatException e) {
            return new TextMessage(HELP_TEXT);
        }

        Optional<String> optImageUrl = getXkcdComicUrlById(id);
        if (!optImageUrl.isPresent()) {
            return new TextMessage("Comic not found");
        }

        String imageUrl = optImageUrl.get();
        return new ImageMessage(imageUrl, imageUrl);
    }


    private static Optional<String> getXkcdComicUrlById(int id) {

        Optional<Map<String, Object>> optJson = getXkcdJson(id);

        if (!optJson.isPresent()) {
            return Optional.empty();
        }

        Map<String, Object> json = optJson.get();
        return Optional.of(json.get("img").toString());
    }

    private static Optional<Map<String, Object>> getXkcdJson(int id) {
        String url = "https://xkcd.com/" + id + "/info.0.json";
        try {
            URL obj = new URL(url);

            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            int responseCode = con.getResponseCode();
            if (responseCode != 200) {
                return Optional.empty();
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String input = in.readLine();

            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> map = mapper.readValue(input,
                    new TypeReference<Map<String, String>>() {});

            return Optional.of(map);
        } catch (IOException e) {
            return Optional.empty();
        }
    }
}
