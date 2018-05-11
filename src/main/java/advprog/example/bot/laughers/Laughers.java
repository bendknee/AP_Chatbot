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
    private String groupId; // group started with C, room started with R, user started with U
    private String userId;
    private long numberOfLaugh;

    protected Laughers() {
    }

    public Laughers(String groupId, String userId) {
        this.groupId = groupId;
        this.userId = userId;
    }

    // For UT purposes
    public Laughers(String groupId, String userId, long numberOfLaugh) {
        this.groupId = groupId;
        this.userId = userId;
        this.numberOfLaugh = numberOfLaugh;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getUserId() {
        return userId;
    }

    public long getNumberOfLaugh() {
        return numberOfLaugh;
    }

    public void incrementNumberOfLaugh() {
        this.numberOfLaugh++;
    }
}
