package billboardpackage;

import java.util.logging.Logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BillboardApp {
    private static final Logger LOGGER = Logger.getLogger(BillboardApp.class.getName());

    static {
        System.setProperty("line.bot.channelSecret", "68d4dc2b82c35"
                + "d6e24eadf8bfea40fa2\n");
        System.setProperty("line.bot.channelToken", "JLOZx62DldFCg59q3tbHAt"
                + "hoXwCgEpTBYQk194MZZnUGjkCTdANhwjeBjX/GjRkSDU"
                + "bW60uDeWiQhJPPOetPB9y/7V1q7SqvhyABn8RRkLFWI2o"
                + "+bewYl+5qazqJJEYfahfQWEXKqA2sQf+3df"
                + "ruFgdB04t89/1O/w1cDnyilFU=");
    }

    public static void main(String[] args) {
        LOGGER.info("Application starting ...");
        SpringApplication.run(BillboardApp.class, args);
    }
}
