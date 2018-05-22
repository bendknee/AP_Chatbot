package advprog.example.bot.context;

import java.util.HashMap;

public abstract class Context<T> {
    private HashMap<String, T> store;

    public Context() {
        store = new HashMap<>();
    }

    public void putContext(String key, T value) {
        store.put(key, value);
    }

    public T getContext(String key) {
        return store.get(key);
    }

    public T removeContext(String key) {
        return store.remove(key);
    }

    public boolean containsKey(String key) {
        return store.containsKey(key);
    }
}
