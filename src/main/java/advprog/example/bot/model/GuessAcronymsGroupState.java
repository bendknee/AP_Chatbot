package advprog.example.bot.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class GuessAcronymsGroupState {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String groupId;
    private String state;

    protected GuessAcronymsGroupState() {
    }

    public GuessAcronymsGroupState(String groupId) {
        this.groupId = groupId;
        state = "";
    }

    public GuessAcronymsGroupState(String groupId, String state) {
        this.groupId = groupId;
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
