package advprog.example.bot.controller;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.ImageMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import java.util.logging.Logger;

@LineMessageHandler
public class NearbyPhotosController {

    private static final Logger LOGGER = Logger.getLogger(NearbyPhotosController.class.getName());

    @EventMapping
    public ImageMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));

        /*
        * implement line Location getter API
        * implement Flickr GeoPhotosbyLocation API
        * implement Line image message postman
        */

        return new ImageMessage("url", "url");
    }
}
