package pasaranprimbon.bot;

import java.util.logging.Logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BotPrimbonApplication {

    private static final Logger LOGGER = Logger.getLogger(BotPrimbonApplication.class.getName());

    public static void main(String[] args) {
        LOGGER.info("Application starting ...");
        SpringApplication.run(BotPrimbonApplication.class, args);
    }
}