package advprog.example.bot.laughers;

public class LaughersManager {

    public boolean checkMessageContainsLaughers(String message) {
        return message.contains("wkwk") | message.contains("haha");
    }

    public void processMessage(String message, long groupId, long userId) {

    }

    public String getTop5LaughersInGroup(long groupId) {
        return null;
    }
}
