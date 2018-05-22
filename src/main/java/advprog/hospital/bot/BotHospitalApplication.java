package advprog.hospital.bot;

import java.util.logging.Logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BotHospitalApplication {

    private static final Logger LOGGER = Logger.getLogger(BotHospitalApplication.class.getName());

    public static void main(String[] args) {
        LOGGER.info("Application starting ...");
        SpringApplication.run(BotHospitalApplication.class, args);
    }
}
