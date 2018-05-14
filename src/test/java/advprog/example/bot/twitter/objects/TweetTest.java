package advprog.example.bot.twitter.objects;

import advprog.example.bot.twitter.TwitterAPIHelper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TweetTest {
    @Test
    public void parseJsonTest() throws Exception {
        TwitterAPIHelper instance = TwitterAPIHelper.getInstance();
        String rawResponse = instance.requestGet("https://api.twitter.com/1.1/statuses/user_timeline.json"
                + "?screen_name=TheObsessivePr1");
        JSONObject latestTweet = new JSONArray(rawResponse).getJSONObject(0);
        Tweet response = Tweet.parseJson(latestTweet);

        assertEquals(latestTweet.getString("text"), response.getText());
        assertEquals(latestTweet.getString("created_at"), response.getTimeStamp());
    }

    @Test
    public void parseJsonListTest() throws Exception {
        TwitterAPIHelper instance = TwitterAPIHelper.getInstance();
        String rawResponse = instance.requestGet("https://api.twitter.com/1.1/statuses/user_timeline.json"
                + "?screen_name=TheObsessivePr1");
        List<Tweet> response = Tweet.parseJsonList(new JSONArray(rawResponse));
        assertNotNull(response);
    }
}
