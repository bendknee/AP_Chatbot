package advprog.example.bot.command.mediawiki;

import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;

public class MediaWikiCommand {

    private static final String ADD_WIKI_HELP_TEXT = "Usage: /add_wiki [url]";

    public static Message executeAddWiki(String args) {
        System.out.println("ARGS  \"" + args + "\"");
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

}
