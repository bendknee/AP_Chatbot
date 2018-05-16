package advprog.japanbillboard.bot;

import java.util.logging.Logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BotJapanApplication {

    private static final Logger LOGGER = Logger.getLogger(BotJapanApplication.class.getName());

    public static void main(String[] args) {
        LOGGER.info("Application starting ...");
        SpringApplication.run(BotJapanApplication.class, args);
    }
}