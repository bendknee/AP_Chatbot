package advprog.example.bot.controller;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.action.*;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.PostbackEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.StickerMessage;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.template.ButtonsTemplate;
import com.linecorp.bot.model.message.template.CarouselColumn;
import com.linecorp.bot.model.message.template.CarouselTemplate;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import com.sun.jndi.toolkit.url.Uri;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

@LineMessageHandler
public class BotController {

    private static final Logger LOGGER = Logger.getLogger(BotController.class.getName());

    @Autowired
    LineMessagingClient lineMessagingClient;

    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));
        TextMessageContent content = event.getMessage();
        String contentText = content.getText();
        String token = event.getReplyToken();
        String toReply= "";

        String replyText = contentText.replace("/echo", "");
        if (replyText.equalsIgnoreCase("lookup_anime")) {
            carousel(token);

        }

        return new TextMessage(toReply);
    }

    @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));
    }

    private void reply(String token, TemplateMessage replies){
        try {
            lineMessagingClient.replyMessage(new ReplyMessage(token, replies)).get();

        } catch (InterruptedException |ExecutionException e) {
            System.out.println("Error");
        }
    }

    private void carousel(String replyToken){
        try {
            String imageUrl = new Uri("/static/buttons/1040.jpg").toString();
            CarouselTemplate carouselTemplate = new CarouselTemplate(
                    Arrays.asList(
                            new CarouselColumn(imageUrl, "hoge", "fuga", Arrays.asList(
                                    new URIAction("Go to line.me",
                                            "https://line.me"),
                                    new URIAction("Go to line.me",
                                            "https://line.me"),
                                    new PostbackAction("Say hello1",
                                            "hello こんにちは")
                            )),
                            new CarouselColumn(imageUrl, "hoge", "fuga", Arrays.asList(
                                    new PostbackAction("言 hello2",
                                            "hello こんにちは",
                                            "hello こんにちは"),
                                    new PostbackAction("言 hello2",
                                            "hello こんにちは",
                                            "hello こんにちは"),
                                    new MessageAction("Say message",
                                            "Rice=米")
                            )),
                            new CarouselColumn(imageUrl, "Datetime Picker", "Please select a date, time or datetime", Arrays.asList(
                                    new DatetimePickerAction("Datetime",
                                            "action=sel",
                                            "datetime",
                                            "2017-06-18T06:15",
                                            "2100-12-31T23:59",
                                            "1900-01-01T00:00"),
                                    new DatetimePickerAction("Date",
                                            "action=sel&only=date",
                                            "date",
                                            "2017-06-18",
                                            "2100-12-31",
                                            "1900-01-01"),
                                    new DatetimePickerAction("Time",
                                            "action=sel&only=time",
                                            "time",
                                            "06:15",
                                            "23:59",
                                            "00:00")
                            ))
                    ));
            TemplateMessage templateMessage = new TemplateMessage("Carousel alt text", carouselTemplate);
            this.reply(replyToken, templateMessage);
        }
        catch (Exception e) {
            System.out.println("error");
        }

    }
}
