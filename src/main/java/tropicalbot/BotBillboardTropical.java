package tropicalbot;

import java.util.logging.Logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BotBillboardTropical {

    private static final Logger LOGGER = Logger.getLogger(BotBillboardTropical.class.getName());

    static {
        System.setProperty("line.bot.channelSecret", "4f5061aa776591"
                + "aad1bf35965ab5f25d");
        System.setProperty("line.bot.channelToken", "csN67un3gG09L80xWS5VjCcb0OM"
                + "3GqOpQjBd76HuJn1Go8Wwb4xQbPK9kRygi144i9dsvFGc6OUgFiHCdJfxcen"
                + "uByIV0ASTfk6xIxLwoC9fGE9+lqF/frcVm0AQUmukpJ1wR2kl"
                + "5+1b9t7Pdf6fdgdB04t89/1O/w1cDnyilFU=");
    }

    public static void main(String[] args) {
        LOGGER.info("Application starting ...");
        SpringApplication.run(BotBillboardTropical.class, args);
    }
}
