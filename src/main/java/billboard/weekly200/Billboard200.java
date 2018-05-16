package billboard.weekly200;

import java.util.logging.Logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Billboard200 {

    private static final Logger LOGGER = Logger.getLogger(Billboard200.class.getName());

    public static void main(String[] args) {
        LOGGER.info("Application starting ...");
        SpringApplication.run(Billboard200.class, args);
    }
}
