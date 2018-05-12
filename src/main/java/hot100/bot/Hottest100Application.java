package hot100.bot;

import java.util.logging.Logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Hottest100Application {

    private static final Logger LOGGER = Logger.getLogger(Hottest100Application.class.getName());

    public static void main(String[] args) {
        LOGGER.info("Application starting ...");
        SpringApplication.run(Hottest100Application.class, args);
    }
}