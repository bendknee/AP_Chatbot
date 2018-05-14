package advprog.example.bot.watcher;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;

public interface Watcher {
    void handle(MessageEvent<TextMessageContent> event);
}
