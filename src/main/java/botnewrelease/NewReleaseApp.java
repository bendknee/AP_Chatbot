package botnewrelease;

import java.util.logging.Logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NewReleaseApp {

    static {
        System.setProperty("line.bot.channelSecret", "6aaea5f3be3aff04"
                + "b50ff183727493a3");
        System.setProperty("line.bot.channelToken", "zoSKZdAdyRLBZ5TMpmVf5VR/j0AVVaax1a"
                + "HLnXoAvnwvB1zzVWmcHdHIQ/Hm1wmg55KuC1EOEqggMIOcuo2DNP8JL1tw"
                + "3wh7kIl8R2gAOiKLTdVb7oLUHRDWSrKZo51y8EUrV+nDn1aF0ehWVM"
                + "jw0AdB04t89/1O/w1cDnyilFU=");
    }

    private static final Logger LOGGER = Logger.getLogger(NewReleaseApp.class.getName());

    public static void main(String[] args) {
        LOGGER.info("Application starting ...");
        SpringApplication.run(NewReleaseApp.class, args);
    }
}
