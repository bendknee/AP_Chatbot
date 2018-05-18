package advprog.example.bot.composer;


import com.linecorp.bot.model.message.Message;

public interface Composer {
    Message composeMessage(String arg);
}
