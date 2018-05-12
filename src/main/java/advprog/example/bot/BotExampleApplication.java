package advprog.example.bot;

import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import country.bot.top10countrysong.Billboard10Country;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class BotExampleApplication extends SpringBootServletInitializer{

    private static final Logger LOGGER = Logger.getLogger(BotExampleApplication.class.getName());



    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(BotExampleApplication.class);
    }

    public static void main(String[] args) {
        LOGGER.info("Application starting...");
        SpringApplication.run(BotExampleApplication.class, args);
    }

}
