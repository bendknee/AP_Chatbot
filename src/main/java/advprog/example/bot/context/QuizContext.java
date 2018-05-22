package advprog.example.bot.context;

import advprog.example.bot.entity.Quiz;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class QuizContext {
    private HashMap<String, Quiz> store = new HashMap<>();

    public void putContext(String key, Quiz quiz) {
        store.put(key, quiz);
    }

    public Quiz getContext(String key) {
        return store.get(key);
    }

    public boolean containsContext(String key) {
        return store.containsKey(key);
    }

    public void removeContext(String key) {
        store.remove(key);
    }
}
