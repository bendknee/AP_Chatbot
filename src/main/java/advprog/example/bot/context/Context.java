package advprog.example.bot.context;

import java.util.HashMap;

public class Context {
    private static HashMap<String, String> store;

    private Context() {}

    public static void storeContext(String key, String value) {
        if (store == null) {
            store = new HashMap<>();
        }

        store.put(key, value);
    }

    public static String removeContext(String key) {
        String value = null;

        if (store != null) {
            value = store.remove(key);
        }

        return value;
    }
}
