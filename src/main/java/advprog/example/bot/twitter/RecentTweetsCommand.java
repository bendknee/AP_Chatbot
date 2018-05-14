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
        String username = contentText.replace("/tweets recent ", "");
        List<Tweet> tweetList = TwitterAPIHelper.getInstance().getRecentTweets(username);

        String message = "";
        for (Tweet tweet : tweetList) {
            message += tweet.toString() + "/n/n";
        }

        return new TextMessage(message);
    }
}
