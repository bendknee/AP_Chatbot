package advprog.flickr.bot;

import java.util.logging.Logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FlickrLocationApplication {

    private static final Logger LOGGER = Logger.getLogger(
            FlickrLocationApplication.class.getName());

    public static void main(String[] args) {
        LOGGER.info("Application starting ...");
        SpringApplication.run(FlickrLocationApplication.class, args);
    }
}
