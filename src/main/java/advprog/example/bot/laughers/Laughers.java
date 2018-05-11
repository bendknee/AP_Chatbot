package advprog.example.bot.laughers;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Laughers {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private long groupId;
    private long userId;
    private long numberOfLaugh;

    protected Laughers() {
    }

    public Laughers(long groupId, long userId) {
        this.groupId = groupId;
        this.userId = userId;
    }

    // For UT purposes
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
