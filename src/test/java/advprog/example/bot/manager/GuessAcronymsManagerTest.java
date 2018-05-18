package advprog.example.bot.manager;

import com.linecorp.bot.client.LineMessagingClient;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import advprog.example.bot.repository.GuessAcronymsGroupStateRepository;
import advprog.example.bot.repository.GuessAcronymsRepository;
import advprog.example.bot.repository.GuessAcronymsUserStateRepository;

@SpringBootTest(properties = "line.bot.handler.enabled=false")
@ExtendWith(SpringExtension.class)
public class GuessAcronymsManagerTest {

    static {
        System.setProperty("line.bot.channelSecret", "SECRET");
        System.setProperty("line.bot.channelToken", "TOKEN");
    }

    private GuessAcronymsManager guessAcronymsManager;

    @Mock
    private GuessAcronymsRepository guessAcronymsRepository;
    @Mock
    private GuessAcronymsUserStateRepository guessAcronymsUserStateRepository;
    @Mock
    private GuessAcronymsGroupStateRepository guessAcronymsGroupStateRepository;
    @Mock
    private LineMessagingClient lineMessagingClient;

    @BeforeEach
    void setUp() {
        guessAcronymsManager = new GuessAcronymsManager(guessAcronymsRepository, guessAcronymsUserStateRepository, guessAcronymsGroupStateRepository, lineMessagingClient);
    }

    @Test
    void testAddAcronym() {

    }

    @Test
    void testUpdateAcronym() {

    }

    @Test
    void testDeleteAcronym() {

    }

    @Test
    void testPlayAcronymGame() {

    }
}
