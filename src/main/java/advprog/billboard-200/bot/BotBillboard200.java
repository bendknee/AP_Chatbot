package advprog.billboard-200.bot;

import java.util.logging.Logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BotBillboard200 {

    private static final Logger LOGGER = Logger.getLogger(BotBillboard200.class.getName());

    public static void main(String[] args) {
        LOGGER.info("Application starting ...");
        SpringApplication.run(BotBillboard200.class, args);
    }
}
