package billboardpackage;

import java.util.logging.Logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BillboardApp {
    private static final Logger LOGGER = Logger.getLogger(BillboardApp.class.getName());

    static {
        System.setProperty("line.bot.channelSecret", "6704d8fc1af3f5d"
                + "9f353585de13e1ae59");
        System.setProperty("line.bot.channelToken", "p/2QDsiSAhNUe+n+hi2POsxPXNvbVyin/hsgEBdTY"
                + "56dTCriaMcvhB8KFH7eEaxA019s0J+qWoAiNRxN"
                + "w4O8G7gdEZNN/KRwZBOR8+ZCkUUgmwvIZo"
                + "p3WyYcmSF1M9WymDRldeHv/5AML9hDQ3"
                + "wQjAdB04t89/1O/w1cDnyilFU=");
    }

    public static void main(String[] args) {
        LOGGER.info("Application starting ...");
        SpringApplication.run(BillboardApp.class, args);
    }
}
