package advprog.example.bot.echo;

import advprog.example.bot.Command;
import com.linecorp.bot.model.event.message.MessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;

public class EchoCommand implements Command {
    public Message produceMessage(MessageContent content) {
        String contentText = ((TextMessageContent) content).getText();

        String replyText = contentText.replace("/echo", "");
        return new TextMessage(replyText.substring(1));
    }
}
