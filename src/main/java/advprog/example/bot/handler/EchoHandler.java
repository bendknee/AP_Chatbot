package advprog.example.bot.handler;

import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import org.springframework.stereotype.Component;

@Component
public class EchoHandler {

    public Message composeReply(String arg) {
        return new TextMessage(arg);
    }
}
