package billboard.newage;

import java.util.logging.Logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BillboardNewAge {

    private static final Logger LOGGER = Logger.getLogger(BillboardNewAge.class.getName());

    public static void main(String[] args) {
        LOGGER.info("Application starting ...");
        SpringApplication.run(BillboardNewAge.class, args);
    }
}
