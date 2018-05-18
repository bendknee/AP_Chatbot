package advprog.example.bot.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class GuessAcronymsUserState {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String groupId;
    private String userId;
    private String state;
    private long score;

    protected GuessAcronymsUserState() {
    }

    public GuessAcronymsUserState(String groupId, String userId, String state, long score) {
        this.groupId = groupId;
        this.userId = userId;
        this.state = state;
        this.score = score;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getUserId() {
        return userId;
    }

    public String getState() {
        return state;
    }

    public long getScore() {
        return score;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setScore(long score) {
        this.score = score;
    }
}
