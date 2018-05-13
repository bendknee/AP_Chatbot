package billboardpackage;

import java.util.logging.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BillboardApp {
    private static final Logger LOGGER = Logger.getLogger(BillboardApp.class.getName());

    public static void main(String[] args) {
        LOGGER.info("Application starting ...");
        SpringApplication.run(BillboardApp.class, args);
    }
}
