package advprog.example.bot.controller;

import advprog.example.bot.feature.PicAnalyze;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.ImageMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import java.util.logging.Logger;

@LineMessageHandler
public class EchoController {

    private static final Logger LOGGER = Logger.getLogger(EchoController.class.getName());

    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));
        TextMessageContent content = event.getMessage();
        String contentText = content.getText();
        String reply;

        if (contentText.equalsIgnoreCase("analyse_picture")) {
            PicAnalyze.flag = true;
            reply = "give us the image you want to analyze pls";
        }
        else {
            PicAnalyze.flag = false;
            reply = "";
        }

        String replyText = contentText.replace("/echo", reply);
        return new TextMessage(replyText.substring(1));
    }

    @EventMapping
    public TextMessage handleImageMessageEvent(MessageEvent<ImageMessageContent> event) {
        LOGGER.fine(String.format("ImageMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));

        String message = event.getMessage().toString();

        if(PicAnalyze.flag == true){
            return new TextMessage(PicAnalyze.analyze(message));
        }
        else return new TextMessage("please put the command first");


    }

    @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));
    }
}
