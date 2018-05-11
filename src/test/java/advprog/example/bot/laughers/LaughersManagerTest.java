package advprog.example.bot.laughers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(properties = "line.bot.handler.enabled=false")
@ExtendWith(SpringExtension.class)
public class LaughersManagerTest {

    static {
        System.setProperty("line.bot.channelSecret", "SECRET");
        System.setProperty("line.bot.channelToken", "TOKEN");
    }

    @Autowired
    private LaughersManager laughersManager;

    @Mock
    private LaughersRepository laughersRepository;

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
    void testProcessMessageContainsLaughers() {
        laughersManager.processMessage("lucu wkwk haha", 1, 2);
        verify(laughersRepository, atLeastOnce()).save(any());
    }

    @Test
    void testProcessMessageNotContainsLaughers() {
        laughersManager.processMessage("kerja!", 1, 2);
        verify(laughersRepository, never()).save(any());
    }

    @Test
    void testGetTop5LaughersInGroup() {
        List<Laughers> laughers = new ArrayList<>();
        laughers.add(new Laughers(1, 2, 3));
        laughers.add(new Laughers(1, 3, 2));
        doReturn(laughers).when(laughersRepository).findByGroupIdOrderByNumberOfLaughDesc(1);

        assertEquals("1. Endrawan (60%)\n2. Andika (40%)", laughersManager.getTop5LaughersInGroup(1));
    }
}
