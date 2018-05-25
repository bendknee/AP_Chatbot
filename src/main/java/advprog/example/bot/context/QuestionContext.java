package advprog.example.bot.context;

import advprog.example.bot.entity.Question;

import java.util.HashMap;

import org.springframework.stereotype.Component;

@Component
public class QuestionContext {
    private HashMap<String, Question> store = new HashMap<>();

    public void putAddContext(String key, Question question) {
        store.put("adding-" + key, question);
    }

    public Question getAddContext(String key) {
        return store.get("adding-" + key);
    }

    public void removeAddContext(String key) {
        store.remove("adding-" + key);
    }

    public boolean containsAddContextKey(String key) {
        return store.containsKey("adding-" + key);
    }

    public void putChangeContext(String key, Question question) {
        store.put("change-" + key, question);
    }

    public Question getChangeContext(String key) {
        return store.get("change-" + key);
    }

    public void removeChangeContext(String key) {
        store.remove("change-" + key);
    }

    public boolean containsChangeContextKey(String key) {
        return store.containsKey("change-" + key);
    }
}
