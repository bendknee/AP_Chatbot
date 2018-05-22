package advprog.example.bot.context;

import advprog.example.bot.entity.QuestionEntity;
import org.springframework.stereotype.Component;

@Component
public class QuestionContext extends Context<QuestionEntity> {
    public void putAddContext(String key, QuestionEntity question) {
        key = "adding-" + key;

        super.putContext(key, question);
    }

    public QuestionEntity getAddContext(String key) {
        key = "adding-" + key;

        return super.getContext(key);
    }

    public QuestionEntity removeAddContext(String key) {
        key = "adding-" + key;

        return super.removeContext(key);
    }

    public boolean containsAddContextKey(String key) {
        key = "adding-" + key;

        return super.containsKey(key);
    }

    public void putChangeContext(String key, QuestionEntity question) {
        key = "change-" + key;

        super.putContext(key, question);
    }

    public QuestionEntity getChangeContext(String key) {
        key = "change-" + key;

        return super.getContext(key);
    }

    public QuestionEntity removeChangeContext(String key) {
        key = "change-" + key;

        return super.removeContext(key);
    }

    public boolean containsChangeContextKey(String key) {
        key = "change-" + key;

        return super.containsKey(key);
    }
}
