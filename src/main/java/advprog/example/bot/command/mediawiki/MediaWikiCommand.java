package advprog.example.bot.command.mediawiki;

import com.linecorp.bot.model.action.MessageAction;
import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.template.CarouselColumn;
import com.linecorp.bot.model.message.template.CarouselTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MediaWikiCommand {

    private static final String ADD_WIKI_HELP_TEXT = "Usage: /add_wiki [url]";
    private static final String RANDOM_WIKI_ARTICLE_HELP_TEXT = "Usage: /random_wiki_article";

    public static Message executeAddWiki(String args) {
        String[] arg = args.split(" ");
        if (args.isEmpty() || arg.length != 1) {
            return new TextMessage(ADD_WIKI_HELP_TEXT);
        }

        MediaWiki mediaWiki = new MediaWiki(args);
        if (!mediaWiki.isMediaWikiApiActive()) {
            return new TextMessage("Cannot find API, make sure the url is added with http(s)://..");
        }

        MediaWikiDao.INSTANCE.persistMediaWiki(mediaWiki);
        return new TextMessage("Wiki added.");
    }

    public static Message executeRandomWikiArticle(String args) {
        List<MediaWiki> mediaWikis = MediaWikiDao.INSTANCE.getMediaWikis();
        if (mediaWikis.isEmpty()) {
            return new TextMessage("Please add MediaWiki API first.");
        }

        if (args.isEmpty()) {
            List<CarouselColumn> columns = new ArrayList<>();
            for (int i = 0; i < mediaWikis.size(); i++) {
                MediaWiki mediaWiki = mediaWikis.get(i);
                columns.add(
                    new CarouselColumn("https://hanassets.nd.gov/images/product/test.png",
                        "MediaWiki",
                        mediaWiki.getApiUrl(),
                        Collections.singletonList(
                            new MessageAction("Random article",
                                    "/random_wiki_article " + i)
                        )
                    )
                );
            }

            CarouselTemplate carouselTemplate = new CarouselTemplate(columns);

            return new TemplateMessage("Random wiki article", carouselTemplate);
        }

        int id;
        try {
            id = Integer.parseInt(args);
        } catch (NumberFormatException e) {
            return new TextMessage(RANDOM_WIKI_ARTICLE_HELP_TEXT);
        }

        if (id < 0 || id >= mediaWikis.size()) {
            return new TextMessage(RANDOM_WIKI_ARTICLE_HELP_TEXT);
        }

        MediaWiki mediaWiki = mediaWikis.get(id);
        String pageUrl = mediaWiki.getRandomPageUrl();
        return new TextMessage(mediaWiki.getPageTitle(pageUrl) + "\n" + pageUrl);
    }
}
