package advprog.example.bot;

import java.util.logging.Logger;

import advprog.example.bot.composer.EchoComposer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BotExampleApplication {

    private static final Logger LOGGER = Logger.getLogger(BotExampleApplication.class.getName());

    public static void main(String[] args) {
        LOGGER.info("Application starting ...");

        CommandPattern.addPattern("/echo", new EchoComposer());

        SpringApplication.run(BotExampleApplication.class, args);
    }
}
