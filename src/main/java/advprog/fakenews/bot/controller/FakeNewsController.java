package advprog.fakenews.bot.controller;

import advprog.fakenews.bot.FakeNewsParser;
import advprog.fakenews.bot.News;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.source.GroupSource;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import java.util.Arrays;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@LineMessageHandler
public class FakeNewsController {
    private static final Logger LOGGER = Logger.getLogger(FakeNewsController.class.getName());
    private static final String PATTERN_STRING = "((http:\\/\\/|https:\\/\\/)?"
            + "(www.)?(([a-zA-Z0-9-]){2,}\\.){1,4}([a-zA-Z]){2,6}(\\/"
            + "([a-zA-Z-_\\/\\.0-9#:?=&;,]*)?)?)";
    private FakeNewsParser parser = new FakeNewsParser();

    private String isUrl(String contentText) {
        Pattern pattern = Pattern.compile(PATTERN_STRING);
        return Arrays.stream(contentText.split(" "))
                .filter(word -> {
                    Matcher wordMatcher = pattern.matcher(word);
                    return wordMatcher.matches();
                }).findFirst()
                .orElse("");
    }

    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));
        TextMessageContent content = event.getMessage();
        try {
            if (event.getSource()instanceof GroupSource) {
                String url = isUrl(content.getText());
                if (!url.equals("")) {
                    return new TextMessage(parser.printWarning(url));
                }
            }
            String depan = content.getText().split(" ")[0];
            if (depan.equalsIgnoreCase("/is_fake")) {
                String news = content.getText().split(" ")[1];
                return new TextMessage(parser.checkNews(news, "fake"));
            } else if (depan.equalsIgnoreCase("/is_satire")) {
                String news = content.getText().split(" ")[1];
                return new TextMessage(parser.checkNews(news, "satire"));
            } else if (depan.equalsIgnoreCase("/is_conspiracy")) {
                String news = content.getText().split(" ")[1];
                return new TextMessage(parser.checkNews(news, "conspiracy"));
            } else if (depan.equalsIgnoreCase("/add_filter")) {
                String filterUrl = content.getText().split(" ")[1];
                String filterType = content.getText().split(" ")[2];
                return new TextMessage(parser.addNewCriteria(filterUrl, filterType));
            } else {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            return new TextMessage("Inputan tidak tersedia nih, coba masukkan /is_fake URL\n"
                    + "/is_satire URL\n"
                    + "/is_conspiracy URL\n"
                    + "/add_filter URL TYPE");
        }
    }

    @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));
    }
}
