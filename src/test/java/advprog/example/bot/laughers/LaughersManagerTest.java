package advprog.example.bot.laughers;

import com.linecorp.bot.client.LineMessagingClient;

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

    @Mock
    private LineMessagingClient lineMessagingClient;

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
        laughersManager.processMessage("lucu wkwk haha", "R1", "U1");
        verify(laughersRepository, atLeastOnce()).save(any());
    }

    @Test
    void testProcessMessageNotContainsLaughers() {
        laughersManager.processMessage("kerja!", "R1", "U1");
        verify(laughersRepository, never()).save(any());
    }

    @Test
    void testGetTop5LaughersInGroup() throws Exception {
        List<Laughers> laughers = new ArrayList<>();
        laughers.add(new Laughers("C1", "U1", 3));
        laughers.add(new Laughers("C1", "U2", 2));

        doReturn(laughers).when(laughersRepository).findByGroupIdOrderByNumberOfLaughDesc("C1");
        doReturn("Endrawan")
            .when(lineMessagingClient
            .getGroupMemberProfile("C1", "U1")
            .get().getDisplayName());
        doReturn("Andika")
            .when(lineMessagingClient
            .getGroupMemberProfile("C1", "U2")
            .get().getDisplayName());

        assertEquals("1. Endrawan (60%)\n2. Andika (40%)", laughersManager.getTop5Laughers("C1"));
    }

    @Test
    void testGetTop5LaughersInRoom() throws Exception {
        List<Laughers> laughers = new ArrayList<>();
        laughers.add(new Laughers("R1", "U1", 3));
        laughers.add(new Laughers("R1", "U2", 2));

        doReturn(laughers).when(laughersRepository).findByGroupIdOrderByNumberOfLaughDesc("R1");
        doReturn("Endrawan")
            .when(lineMessagingClient
            .getRoomMemberProfile("R1", "U1")
            .get().getDisplayName());
        doReturn("Andika")
            .when(lineMessagingClient
            .getRoomMemberProfile("R1", "U2")
            .get().getDisplayName());

        assertEquals("1. Endrawan (60%)\n2. Andika (40%)", laughersManager.getTop5Laughers("R1"));
    }

    @Test
    void testGetTop5LaughersInPersonalChat() throws Exception {
        List<Laughers> laughers = new ArrayList<>();
        laughers.add(new Laughers("U1", "U1", 3));

        doReturn(laughers).when(laughersRepository).findByGroupIdOrderByNumberOfLaughDesc("U1");
        doReturn("Endrawan")
            .when(lineMessagingClient
            .getProfile("U1")
            .get().getDisplayName());

        assertEquals("1. Endrawan (100%)", laughersManager.getTop5Laughers("U1"));
    }
}
