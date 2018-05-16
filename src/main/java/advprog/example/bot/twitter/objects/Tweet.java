package advprog.example.bot.twitter.objects;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class Tweet {
    private String text;
    private String timeStamp;

    public Tweet(String text, String timeStamp) {
        this.text = text;
        this.timeStamp = timeStamp;
    }

    public static List<Tweet> parseJsonList(JSONArray json) {
        List<Tweet> result = new ArrayList<Tweet>();

        for (int i = 0; i < json.length(); i++) {
            result.add(parseJson(json.getJSONObject(i)));
        }
        return result;
    }

    public static Tweet parseJson(JSONObject json) {
        String text = json.getString("text");
        String timeStamp = json.getString("created_at");
        return new Tweet(text, timeStamp);
    }

    public String getText() {
        return text;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public String toString() {
        return text + " (" + timeStamp + ")";
    }
}
