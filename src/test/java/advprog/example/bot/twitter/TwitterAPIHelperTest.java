package advprog.example.bot.twitter;

import advprog.example.bot.twitter.objects.Tweet;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TwitterAPIHelperTest {
    @Test
    public void authTest() {
        TwitterAPIHelper instance = TwitterAPIHelper.getInstance();
        instance.authenticate();
    }

    @Test
    public void requestGetTest() {
        TwitterAPIHelper instance = TwitterAPIHelper.getInstance();
        String response = instance.requestGet("https://api.twitter.com/1.1/statuses/user_timeline.json");
        assertFalse(response.isEmpty());
    }

    @Test
    public void getTweetsFromValidAccountWithMoreThan5TweetsTest() {
        TwitterAPIHelper instance = TwitterAPIHelper.getInstance();
        List<Tweet> response = instance.getRecentTweets("twitterapi");
        assertEquals(response.size(), 5);

        response.stream().forEach(tweet -> {
            assertNotNull(tweet.getText());
            assertNotNull(tweet.getTimeStamp());
        });
    }

    @Test
    public void getTweetsFromValidAccountWithLessThan5TweetsTest() {
        TwitterAPIHelper instance = TwitterAPIHelper.getInstance();
        List<Tweet> response = instance.getRecentTweets("TheObsessivePr1");

        String rawSecondResponse = instance.requestGet("https://api.twitter.com/1.1/statuses/user_timeline.json"
                + "?screen_name=TheObsessivePr1");
        List<Tweet> secondResponse = Tweet.parseJsonList(new JSONArray(rawSecondResponse));

        assertEquals(secondResponse.size(), response.size());

        for (int i = 0; i < secondResponse.size(); i++) {
            assertEquals(secondResponse.get(i), response.get(i));
        }
    }
}
