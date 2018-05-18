package advprog.fakenews.bot;

import java.util.logging.Logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BotFakeNewsApplication {

    private static final Logger LOGGER = Logger.getLogger(BotFakeNewsApplication.class.getName());

    public static void main(String[] args) {
        LOGGER.info("Application starting ...");
        SpringApplication.run(BotFakeNewsApplication.class, args);
    }
}
