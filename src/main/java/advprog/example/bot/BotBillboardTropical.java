package advprog.example.bot;

import java.util.logging.Logger; 

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BotBillboardTropical {

    private static final Logger LOGGER = Logger.getLogger(BotBillboardTropical.class.getName());

    public static void main(String[] args) {
        LOGGER.info("Application starting ...");
        SpringApplication.run(BotBillboardTropical.class, args);
    }
}
