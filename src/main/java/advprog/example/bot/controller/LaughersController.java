package advprog.example.bot.controller;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.source.Source;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.logging.Logger;

import advprog.example.bot.laughers.LaughersManager;

@LineMessageHandler
public class LaughersController {

    private static final Logger LOGGER = Logger.getLogger(LaughersController.class.getName());

    private LaughersManager laughersManager;

    @Autowired
    public LaughersController(LaughersManager laughersManager) {
        this.laughersManager = laughersManager;
    }

    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')",
                                  event.getTimestamp(), event.getMessage()));

        TextMessageContent content = event.getMessage();
        String contentText = content.getText();

        Source source = event.getSource();
        String groupId = source.getSenderId();
        String userId = source.getUserId();

        String replyText = "";

        if (contentText.equals("/toplaughers")) {
            replyText = laughersManager.getTop5Laughers(groupId);
        } else {
            laughersManager.processMessage(contentText, groupId, userId);
        }

        return new TextMessage(replyText);
    }
}
