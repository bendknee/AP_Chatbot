package JapanBillboard;

import java.util.logging.Logger; 

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JapanBotBillBoard {

    private static final Logger LOGGER = Logger.getLogger(JapanBotBillBoard.class.getName());

    public static void main(String[] args) {
        LOGGER.info("Application starting ...");
        SpringApplication.run(JapanBotBillBoard.class, args);
    }
}
