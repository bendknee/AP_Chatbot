package advprog.example.bot.twitter.objects;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class Tweet {
    private String text;
    private String timeStamp;

    public Tweet(String text, String timeStamp) {
        this.text = text;
        this.timeStamp = timeStamp;
    }

    public static List<Tweet> parseJsonList(JSONArray json) {
        List<Tweet> result = new ArrayList<Tweet>();
        List<Object> original = json.toList();

        for (Object item : original) {
            result.add(parseJson(new JSONObject(item)));
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
