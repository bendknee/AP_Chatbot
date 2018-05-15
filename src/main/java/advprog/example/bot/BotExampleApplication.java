package advprog.example.bot;

import java.util.logging.Logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BotExampleApplication {

    private static final Logger LOGGER = Logger.getLogger(BotExampleApplication.class.getName());

    public String URLReader(String youtubeURL) {
        Document doc;
        Element body;
        try {
            doc = Jsoup.connect(youtubeURL).get();
            return  doc.getElementsByTag("title").text();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    public VideoData getVideo

    public static void main(String[] args) {
        LOGGER.info("Application starting ...");
        SpringApplication.run(BotExampleApplication.class, args);
    }
}
