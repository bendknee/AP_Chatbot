package billboard.hot100;

import java.util.logging.Logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Billboard100 {

    private static final Logger LOGGER = Logger.getLogger(Billboard100.class.getName());

    public static void main(String[] args) {
        LOGGER.info("Application starting ...");
        SpringApplication.run(Billboard100.class, args);
    }
}
