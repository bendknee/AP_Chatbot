package advprog.example.bot;

import com.linecorp.bot.model.event.message.MessageContent;
import com.linecorp.bot.model.message.Message;

public interface Command {
    Message produceMessage(MessageContent content);
}
