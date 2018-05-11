package advprog.example.bot.twitter.objects;

import advprog.example.bot.twitter.TwitterAPIHelper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TweetTest {
    @Test
    public void parseJsonTest() {
        TwitterAPIHelper instance = TwitterAPIHelper.getInstance();
        String rawResponse = instance.requestGet("https://api.twitter.com/1.1/statuses/user_timeline.json"
                + "?screen_name=TheObsessivePr1");
        JSONObject latestTweet = new JSONArray(rawResponse).getJSONObject(0);
        Tweet response = Tweet.parseJson(latestTweet);

        assertTrue(rawResponse.contains(response.getText()));
        assertTrue(rawResponse.contains(response.getTimeStamp().toString()));
    }

    @Test
    public void parseJsonListTest() {
        TwitterAPIHelper instance = TwitterAPIHelper.getInstance();
        String rawResponse = instance.requestGet("https://api.twitter.com/1.1/statuses/user_timeline.json"
                + "?screen_name=TheObsessivePr1");
        List<Tweet> response = Tweet.parseJsonList(new JSONArray(rawResponse));
        assertNotNull(response);
    }
}
