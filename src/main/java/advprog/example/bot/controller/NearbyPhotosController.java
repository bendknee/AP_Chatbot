package advprog.example.bot.controller;

import com.aetrion.flickr.Flickr;
import com.aetrion.flickr.REST;
import com.aetrion.flickr.test.TestInterface;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.ImageMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import java.util.Collection;
import java.util.Collections;
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

        String apiKey = "YOUR_API_KEY";
        String sharedSecret = "YOUR_SHARED_SECRET";
        Flickr f = new Flickr(apiKey, sharedSecret, new REST());
        TestInterface testInterface = f.getTestInterface();
        Collection results = testInterface.echo(Collections.EMPTY_MAP);

        return new ImageMessage("url", "url");
    }
}
