package advprog.example.bot.controller;

import com.aetrion.flickr.Flickr;
import com.aetrion.flickr.REST;
import com.aetrion.flickr.test.TestInterface;
import com.linecorp.bot.model.action.URIAction;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.LocationMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.ImageMessage;
import com.aetrion.flickr.places.Place;
import com.linecorp.bot.model.message.template.ImageCarouselColumn;
import com.linecorp.bot.model.message.template.ImageCarouselTemplate;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import javax.xml.parsers.ParserConfigurationException;
import java.util.Collection;
import java.util.Collections;
import java.util.logging.Logger;

@LineMessageHandler
public class NearbyPhotosController {

    private static final Logger LOGGER = Logger.getLogger(NearbyPhotosController.class.getName());
    private Flickr flickr;


    public NearbyPhotosController() throws ParserConfigurationException {
        String apiKey = "4847f0e678f60a5f7e213521c263deef ";
        String sharedSecret = "4189ad7a7e127c70";
        flickr = new Flickr(apiKey, sharedSecret, new REST());
    }

    @EventMapping
    public ImageCarouselTemplate handleLocationMessageEvent(MessageEvent<LocationMessageContent> event) {
        LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));

        /*
        * implement line Location getter API
        * implement Flickr GeoPhotosbyLocation API
        * implement Line image message postman
        */

        LocationMessageContent content = event.getMessage();
        // -6.364546
        // 106.828611
        new ImageCarouselColumn("wdw", new URIAction("","URI"));
        ImageCarouselTemplate carousel = new ImageCarouselTemplate(null);

        return carousel;
    }
}
