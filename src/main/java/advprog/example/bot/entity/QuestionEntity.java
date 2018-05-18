package advprog.example.bot.entity;


import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

public class QuestionEntity {

    @Id
    @GeneratedValue
    private long id;

    private String question;

    @OneToMany(mappedBy = "question")
    private List<AnswerEntity> answers;

    public QuestionEntity(String question) {
        this.question = question;
        this.answers = new ArrayList<>();
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<AnswerEntity> getAnswers() {
        return answers;
    }

    public void addAnswer(AnswerEntity answer) {
        this.answers.add(answer);
    }
}
