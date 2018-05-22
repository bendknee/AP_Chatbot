package advprog.example.bot.command.mediawiki;

import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;

import java.util.List;
import java.util.Random;

public class MediaWikiCommand {

    private static final String ADD_WIKI_HELP_TEXT = "Usage: /add_wiki [url]";

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

        Random random = new Random();
        MediaWiki mediaWiki = mediaWikis.get(random.nextInt(mediaWikis.size()));

        String pageUrl = mediaWiki.getRandomPageUrl();
        return new TextMessage(mediaWiki.getPageTitle(pageUrl) + "\n" + pageUrl);
    }
}
