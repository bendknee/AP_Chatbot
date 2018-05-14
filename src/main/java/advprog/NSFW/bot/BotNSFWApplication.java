package advprog.NSFW.bot;

import java.util.logging.Logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BotNSFWApplication {

    private static final Logger LOGGER = Logger.getLogger(BotNSFWApplication.class.getName());

    public static void main(String[] args) {
        LOGGER.info("Application starting ...");
        SpringApplication.run(BotNSFWApplication.class, args);
    }
}
