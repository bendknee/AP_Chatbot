package advprog.nsfw.bot;

import java.io.IOException;
import java.util.logging.Logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BotNsfwApplication {

    private static final Logger LOGGER = Logger.getLogger(BotNsfwApplication.class.getName());

    public static void main(String[] args) throws IOException {
        LOGGER.info("Application starting ...");
        SpringApplication.run(BotNsfwApplication.class, args);
    }
}
