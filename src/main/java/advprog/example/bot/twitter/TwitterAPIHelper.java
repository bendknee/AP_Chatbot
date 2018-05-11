package advprog.example.bot.twitter;

import advprog.example.bot.twitter.objects.Tweet;

import java.util.List;

public class TwitterAPIHelper {
    private static TwitterAPIHelper instance;

    public static TwitterAPIHelper getInstance() {
        if (instance != null) {
            return instance;
        } else {
            return instance = new TwitterAPIHelper();
        }
    }

    public String requestGet(String url) {
        return null;
    }

    public String authenticate() {
        return null;
    }

    public List<Tweet> getRecentTweets(String username) {
        return null;
    }
}
