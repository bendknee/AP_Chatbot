package advprog.example.bot.manager;

import advprog.example.bot.repository.GuessAcronymsGroupStateRepository;
import advprog.example.bot.repository.GuessAcronymsUserStateRepository;
import advprog.example.bot.repository.GuessAcronymsRepository;

import com.linecorp.bot.client.LineMessagingClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GuessAcronymsManager {

    private GuessAcronymsRepository guessAcronymsRepository;
    private GuessAcronymsUserStateRepository guessAcronymsUserStateRepository;
    private GuessAcronymsGroupStateRepository guessAcronymsGroupStateRepository;
    private LineMessagingClient lineMessagingClient;

    @Autowired
    public GuessAcronymsManager(GuessAcronymsRepository guessAcronymsRepository,
                                GuessAcronymsUserStateRepository guessAcronymsUserStateRepository,
                                GuessAcronymsGroupStateRepository guessAcronymsGroupStateRepository,
                                LineMessagingClient lineMessagingClient) {
        this.guessAcronymsRepository = guessAcronymsRepository;
        this.guessAcronymsUserStateRepository = guessAcronymsUserStateRepository;
        this.guessAcronymsGroupStateRepository = guessAcronymsGroupStateRepository;
        this.lineMessagingClient = lineMessagingClient;
    }

    public void handlePrivateChat(String userId, String message) {

    }

    public void handleGroupChat(String groupId, String userId, String message) {

    }
}
