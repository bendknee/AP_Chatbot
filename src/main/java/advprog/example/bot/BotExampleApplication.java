package advprog.example.bot;

import java.util.logging.Logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BotExampleApplication {

    private static final Logger LOGGER = Logger.getLogger(BotExampleApplication.class.getName());

    public static void main(String[] args) {
        System.setProperty("line.bot.channelSecret", "d45fc0dca82c6ec491768f932cefea76");
        System.setProperty("line.bot.channelToken", "tTwT4bf+lJ/h7DmzxaQ3Aqix9PssMzDAIDh64poNkxW/r4H3cKqsWIgbjSV1IO+qRNZ4SE6HkN1DGRD532DHw3whj/9OK3vyN6cEFTgvqWOykgL2LkKDQJjOqHzpUX0lJ3BqJuI5Nl2gOIobGtjONQdB04t89/1O/w1cDnyilFU=");

        LOGGER.info("Application starting ...");
        SpringApplication.run(BotExampleApplication.class, args);
    }
}
