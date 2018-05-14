package advprog.example.bot.composer;

import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;

public class EchoComposer {

    public static Message composeMessage(String arg) {
        return new TextMessage(arg);
    }
}
