package advprog.example.bot.feature;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

public class PicAnalyzeTest {

    @Test
    void analyzeTest() {
        String url = "https://dw9to29mmj727.cloudfront.net/misc/newsletter-naruto3.png";
        String expected = "{\"categories\":[{\"name\":\"people_\",\"score\":0.87890625}]";
        String actual = (PicAnalyze.analyze(url)).substring(0,53);
        assertEquals(expected,actual);
    }
}
