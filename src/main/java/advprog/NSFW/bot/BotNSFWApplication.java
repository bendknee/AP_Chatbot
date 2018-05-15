package advprog.NSFW.bot;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BotNSFWApplication {

    private static final Logger LOGGER = Logger.getLogger(BotNSFWApplication.class.getName());
    static Path downloadedContentDir;

    public static void main(String[] args) throws IOException {
        LOGGER.info("Application starting ...");
        downloadedContentDir = Files.createTempDirectory("line-bot");
        SpringApplication.run(BotNSFWApplication.class, args);
    }
}
