package advprog.example.bot.laughers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest(properties = "line.bot.handler.enabled=false")
@ExtendWith(SpringExtension.class)
public class LaughersManagerTest {

    static {
        System.setProperty("line.bot.channelSecret", "SECRET");
        System.setProperty("line.bot.channelToken", "TOKEN");
    }

    @Autowired
    private LaughersManager laughersManager;

    @Test
    void testContextLoads() {
        assertNotNull(laughersManager);
    }

    @Test
    void testCheckMessageContainsLaughersTrue() {
        assertTrue(laughersManager.checkMessageContainsLaughers("lucu wkwk haha"));
    }

    @Test
    void testCheckMessageContainsLaughersFalse() {
        assertFalse(laughersManager.checkMessageContainsLaughers("kerja!"));
    }

    @Test
    void testGetTop5LaughersInGroup() {
        assertEquals("1. Endrawan (100%)", laughersManager.getTop5LaughersInGroup(1));
    }
}
