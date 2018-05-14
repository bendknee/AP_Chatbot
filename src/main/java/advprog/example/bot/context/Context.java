package advprog.example.bot.context;

import java.util.HashMap;

public class Context {
    private static HashMap<String, String> imageStore;
    private static HashMap<String, String> audioStore;

    private Context() {}

    public static void storeImageContext(String key, String value) {
        if (imageStore == null) {
            imageStore = new HashMap<>();
        }

        imageStore.put(key, value);
    }

    public static void storeAudioContext(String key, String value) {
        if (audioStore == null) {
            audioStore = new HashMap<>();
        }

        audioStore.put(key, value);
    }

    public static String popImageContext(String key) {
        String value = null;

        if (imageStore != null) {
            value = imageStore.remove(key);
        }

        return value;
    }

    public static String popAudioContext(String key) {
        String value = null;

        if (audioStore != null) {
            value = audioStore.remove(key);
        }

        return value;
    }
}
