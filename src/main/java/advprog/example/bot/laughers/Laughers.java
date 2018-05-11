package advprog.example.bot.laughers;

public class Laughers {

    private long groupId;
    private long userId;
    private long numberOfLaugh;

    public Laughers(long groupId, long userId, long numberOfLaugh) {
        this.groupId = groupId;
        this.userId = userId;
        this.numberOfLaugh = numberOfLaugh;
    }

    public long getGroupId() {
        return groupId;
    }

    public long getUserId() {
        return userId;
    }

    public long getNumberOfLaugh() {
        return numberOfLaugh;
    }
}
