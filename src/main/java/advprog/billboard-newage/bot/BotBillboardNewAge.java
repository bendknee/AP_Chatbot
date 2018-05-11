package advprog.billboard-newage.bot;

import java.util.logging.Logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BotBillboardNewAge {

    private static final Logger LOGGER = Logger.getLogger(BotBillboardNewAge.class.getName());

    public static void main(String[] args) {
        LOGGER.info("Application starting ...");
        SpringApplication.run(BotBillboardNewAge.class, args);
    }
}
