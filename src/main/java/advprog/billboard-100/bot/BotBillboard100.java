package advprog.billboard-100.bot;

import java.util.logging.Logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BotBillboard100 {

    private static final Logger LOGGER = Logger.getLogger(BotBillboard100.class.getName());

    public static void main(String[] args) {
        LOGGER.info("Application starting ...");
        SpringApplication.run(BotBillboard100.class, args);
    }
}
