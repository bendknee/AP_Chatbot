package TextSimilarity.bot;

import java.util.logging.Logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication

public class BotTextSimilarityApplication {

    private static final Logger LOGGER = Logger.getLogger(BotTextSimilarityApplication.class.getName());

    public static void main(String[] args) {
        LOGGER.info("Application starting ...");
        SpringApplication.run(BotTextSimilarityApplication.class, args);
    }

}
