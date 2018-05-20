package topalbumpac;

import java.util.logging.Logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TopAlbApp {

    static {
        System.setProperty("line.bot.channelSecret", "9e514c1bbfd82d"
                + "65c9c62738e335ad0c");
        System.setProperty("line.bot.channelToken", "ZBfd4J0OQJsmM96kLQv8lJ"
                + "Z6HROSPl082xGLEE1Mhmc5gofdxygtkYDkErflbC4hi89cYnBU"
                + "L139T8yemI0yjX9J5LeWsWf1LDJviTYReTkbl15Pu1Kje9"
                + "wBce9VP1hEtIoARacjxCQJu9hRj5iy3wd"
                + "B04t89/1O/w1cDnyilFU=");
    }

    private static final Logger LOGGER = Logger.getLogger(TopAlbApp.class.getName());

    public static void main(String[] args) {
        LOGGER.info("Application starting ...");
        SpringApplication.run(TopAlbApp.class, args);
    }
}
