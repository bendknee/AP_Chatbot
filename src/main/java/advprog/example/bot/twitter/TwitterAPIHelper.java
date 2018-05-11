package advprog.example.bot.twitter;

import advprog.example.bot.twitter.objects.Tweet;

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

    public String requestGet(String url) {
        // Parse parameters to the URL
        URL urlObj = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
        conn.addHeader("authorization", "Bearer " + bearerToken);

        // Execute and get the response
        conn.setRequestMethod("GET");
        InputStream resultStream = conn.getInputStream();
        return IOUtils.toString(resultStream, StandardCharsets.UTF_8);
    }

    public void authenticate() {
        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost(API_URL);

        // Request parameters and other properties
        List<NameValuePair> postParams = new ArrayList<>();
        postParams.add(new BasicNameValuePair("grant_type", "client_credentials"));

        // Header for POST request on Twitter API
        String auth = "Basic UEM1RzZhUGdPYkN4TU8yNWFoWUZJTmFTSDoyU0dPaG5VeVVNdFFGWmV3WWFCendjbDg3b"
                + "k0xUkgwbEo1UWxOSFVnYUNTUzhHZXpXbg==";

        httppost.addHeader("authorization", auth);
        httppost.setEntity(new UrlEncodedFormEntity(postParams, "UTF-8"));

        // Execute and get the response
        HttpResponse response = httpclient.execute(httppost);
        InputStream resultStream = response.getEntity().getContent();
        String json = IOUtils.toString(resultStream, StandardCharsets.UTF_8);
        this.bearerToken = JSONObject(json).getString("access_token");
    }

    public List<Tweet> getRecentTweets(String username) {
        String response = post("https://");
        JSONArray json = new JSONArray(response);
        return Tweet.parseJsonList(json);
    }
}
