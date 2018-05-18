package advprog.example.bot.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class GuessAcronyms {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String acronym;
    private String expansion;

    protected GuessAcronyms() {
    }

    public GuessAcronyms(String acronym, String expansion) {
        this.acronym = acronym;
        this.expansion = expansion;
    }

    public String getAcronym() {
        return acronym;
    }

    public String getExpansion() {
        return expansion;
    }

    public void setExpansion(String expansion) {
        this.expansion = expansion;
    }
}
