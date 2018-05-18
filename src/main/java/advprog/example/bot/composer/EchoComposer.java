package advprog.example.bot.composer;

import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import org.springframework.stereotype.Component;

@Component
public class EchoComposer implements Composer {

    @Override
    public Message composeMessage(String arg) {
        return new TextMessage(arg);
    }
}
