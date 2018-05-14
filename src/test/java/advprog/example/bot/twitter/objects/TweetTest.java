package advprog.example.bot.twitter.objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import advprog.example.bot.twitter.TwitterApiHelper;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

public class TweetTest {
    @Test
    public void parseJsonTest() {
        TwitterApiHelper instance = TwitterApiHelper.getInstance();
        String rawResponse = instance.requestGet("https://api.twitter.com/1.1/statuses/user_timeline.json"
                + "?screen_name=TheObsessivePr1");
        JSONObject latestTweet = new JSONArray(rawResponse).getJSONObject(0);
        Tweet response = Tweet.parseJson(latestTweet);

        assertEquals(latestTweet.getString("text"), response.getText());
        assertEquals(latestTweet.getString("created_at"), response.getTimeStamp());
    }

    @Test
    public void parseJsonListTest() {
        TwitterApiHelper instance = TwitterApiHelper.getInstance();
        String rawResponse = instance.requestGet("https://api.twitter.com/1.1/statuses/user_timeline.json"
                + "?screen_name=TheObsessivePr1");
        List<Tweet> response = Tweet.parseJsonList(new JSONArray(rawResponse));
        assertNotNull(response);
    }
}
