package JapanBillboard;

import java.util.logging.Logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JapanBotBillBoard {

    private static final Logger LOGGER = Logger.getLogger(JapanBotBillBoard.class.getName());

    static {
        System.setProperty("line.bot.channelSecret", "c30b82a49293"
                + "b7c16a9bf6488b7d3e63");
        System.setProperty("line.bot.channelToken", "g3S2UFypbYUxSPFjlgr0TA96yKG+R"
                + "ILbXbowKiis43NmW/285W84e7zAVPuW+L"
                + "8ZZuiPyakJNVmzouENCttynmsFPVkQZEM5zDUGbjdkCW0WCK8ISqtlF9vQ3"
                + "frGBsSbcR401NTPOiid0VFND71YhQdB04"
                + "t89/1O/w1cDnyilFU=");
    }

    public static void main(String[] args) {
        LOGGER.info("Application starting ...");
        SpringApplication.run(JapanBotBillBoard.class, args);
    }
}
