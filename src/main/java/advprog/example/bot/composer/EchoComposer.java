package advprog.example.bot.composer;

import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;

public class EchoComposer implements Composer {

    @Override
    public Message composeMessage(String arg, String userId) {
        return new TextMessage(arg);
    }
}
