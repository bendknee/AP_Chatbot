package advprog.example.bot.twitter.objects;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.List;

public class Tweet {
    private String text;
    private Calendar timeStamp;

    public Tweet(String text, Calendar timeStamp) {
        this.text = text;
        this.timeStamp = timeStamp;
    }

    public static List<Tweet> parseJsonList(JSONArray json) {
        return null;
    }

    public static Tweet parseJson(JSONObject json) {
        return null;
    }

    public String getText() {
        return text;
    }

    public Calendar getTimeStamp() {
        return timeStamp;
    }

    public String toString() {
        return null;
    }

    public boolean equals(Object o) {
        return false;
    }
}
