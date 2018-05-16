package advprog.example.bot.twitter;

import advprog.example.bot.Command;
import advprog.example.bot.twitter.objects.Tweet;
import com.linecorp.bot.model.event.message.MessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;

import java.util.List;

public class RecentTweetsCommand implements Command {
    public Message produceMessage(MessageContent content) {
        String contentText = ((TextMessageContent) content).getText();
        String username = contentText.replace("/tweet recent ", "");
        List<Tweet> tweetList = TwitterApiHelper.getInstance().getRecentTweets(username);

        if (tweetList == null) {
            return new TextMessage("Sorry, this username is not available!");
        } else if (tweetList.isEmpty()) {
            return new TextMessage("Hmm... seems like this account has no tweets yet.");
        }

        String message = "";
        for (Tweet tweet : tweetList) {
            message += tweet.toString() + "\r\n";
        }

        return new TextMessage(message);
    }
}
