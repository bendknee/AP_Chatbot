package advprog.example.bot.twitter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import advprog.example.bot.twitter.objects.Tweet;

import java.util.List;

import org.json.JSONArray;
import org.junit.Test;

public class TwitterApiHelperTest {
    @Test
    public void authTest() {
        TwitterApiHelper instance = TwitterApiHelper.getInstance();
        instance.authenticate();
    }

    @Test
    public void requestGetTest() {
        TwitterApiHelper instance = TwitterApiHelper.getInstance();
        String response = instance.requestGet("https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name=twitterapi");
        assertFalse(response.isEmpty());
    }

    @Test
    public void getTweetsFromValidAccountWithMoreThan5TweetsTest() {
        TwitterApiHelper instance = TwitterApiHelper.getInstance();
        List<Tweet> response = instance.getRecentTweets("twitterapi");
        assertEquals(response.size(), 5);

        response.stream().forEach(tweet -> {
            assertNotNull(tweet.getText());
            assertNotNull(tweet.getTimeStamp());
        });
    }

    @Test
    public void getTweetsFromValidAccountWithLessThan5TweetsTest() {
        TwitterApiHelper instance = TwitterApiHelper.getInstance();
        List<Tweet> response = instance.getRecentTweets("TheObsessivePr1");

        String rawSecondResponse = instance.requestGet("https://api.twitter.com/1.1/statuses/user_timeline.json"
                + "?screen_name=TheObsessivePr1");
        List<Tweet> secondResponse = Tweet.parseJsonList(new JSONArray(rawSecondResponse));

        assertEquals(secondResponse.size(), response.size());
    }
}
