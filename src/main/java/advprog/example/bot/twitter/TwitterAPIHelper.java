package advprog.example.bot.twitter;

import advprog.example.bot.twitter.objects.Tweet;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TwitterAPIHelper {
    private static TwitterAPIHelper instance;
    private static String bearerToken;

    public static TwitterAPIHelper getInstance() {
        if (instance != null) {
            return instance;
        } else {
            return instance = new TwitterAPIHelper();
        }
    }

    public String requestGet(String url) throws IOException {
        // Parse parameters to the URL
        URL urlObj = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
        conn.addRequestProperty("authorization", "Bearer " + bearerToken);

        // Execute and get the response
        conn.setRequestMethod("GET");
        InputStream resultStream = conn.getInputStream();
        return IOUtils.toString(resultStream, StandardCharsets.UTF_8);
    }

    public void authenticate() throws Exception {
        String access_token = "UEM1RzZhUGdPYkN4TU8yNWFoWUZJTmFTSDoyU0dPaG5VeVVNdFFGWmV3WWFCendjb"
                + "Dg3bk0xUkgwbEo1UWxOSFVnYUNTUzhHZXpXbg==";
        String url_token = "https://api.twitter.com/oauth2/token/?grant_type=client_credentials";

        URL obj = new URL(url_token);

        HttpURLConnection bearer = (HttpURLConnection) obj.openConnection();

        bearer.setRequestMethod("POST");
        bearer.setRequestProperty("Authorization", "Basic " + access_token);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(bearer.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }

        JSONObject response_bearer = new JSONObject(content.toString());
        bearerToken = response_bearer.getString("access_token");
    }

    public List<Tweet> getRecentTweets(String username) throws Exception {
        String response = requestGet("https://api.twitter.com/1.1/statuses/user_timeline.json?"
                + "screen_name=" + username + "&count=5");
        JSONArray json = new JSONArray(response);
        return Tweet.parseJsonList(json);
    }
}
