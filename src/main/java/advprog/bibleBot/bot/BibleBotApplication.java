package java.advprog.bibleBot.bot;

import java.util.logging.Logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BibleBotApplication {

    private static final Logger LOGGER = Logger.getLogger(BibleBotApplication.class.getName());

    public static void main(String[] args) {
        LOGGER.info("Application starting ...");
        SpringApplication.run(BibleBotApplication.class, args);
    }
}
