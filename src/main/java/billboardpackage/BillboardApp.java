package billboardpackage;

import java.util.logging.Logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BillboardApp {
    private static final Logger LOGGER = Logger.getLogger(BillboardApp.class.getName());

    static {
        System.setProperty("line.bot.channelSecret", "976d1b06307ba2"
                + "536326e9a69aec048f");
        System.setProperty("line.bot.channelToken", "ziqv6nFQ2dpydj2FvuMdI"
                + "zPNhVpcppcg86ZNUSXO/qHfir4+FvT1w9/fypH/pKHisx7peMAk"
                + "CuZ1AB7SoqNf+nlMPmpEbTlLz4/tHcWkSu6VGZY7ehHlaCVhAqr"
                + "RM9RahU7gvEb1Eju00cvvuPp4vgd"
                + "B04t89/1O/w1cDnyilFU=");
    }

    public static void main(String[] args) {
        LOGGER.info("Application starting ...");
        SpringApplication.run(BillboardApp.class, args);
    }
}
