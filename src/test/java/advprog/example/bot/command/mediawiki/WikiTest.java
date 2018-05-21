package advprog.example.bot.command.mediawiki;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class WikiTest {

    @Test
    public void testGetWikiName() {
        Wiki wiki = new Wiki("http://marvel.wikia.com/api.php");

        String name = wiki.getWikiName();

        assertTrue(name.contains("Marvel Database"));
    }
}
