package advprog.example.bot.command.mediawiki;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MediaWikiTest {

    @Test
    void testIsMediaWikiApiActiveOk() {
        MediaWiki mediaWiki = new MediaWiki("http://stage48.net/wiki/api.php");
        assertTrue(mediaWiki.isMediaWikiApiActive());
    }

    @Test
    void testIsMediaWikiApiActiveNotOk() {
        MediaWiki mediaWiki = new MediaWiki("http://www.google.com");
        assertFalse(mediaWiki.isMediaWikiApiActive());
    }

    @Test
    void testRandomPageTitleNotThrowingExceptions() {
        MediaWiki mediaWiki = new MediaWiki("http://stage48.net/wiki/api.php");
        String url = mediaWiki.getRandomPageUrl();
        String pageTitle = mediaWiki.getPageTitle(url);
        assertNotNull(pageTitle);
        assertNotEquals("", pageTitle);
    }
}
