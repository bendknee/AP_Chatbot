package advprog.example.bot.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "answer")
public class AnswerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "question_id")
    private QuestionEntity question;

    @NotNull
    private String answer;

    public AnswerEntity() {}

    public AnswerEntity(String answer, QuestionEntity question) {
        this.answer = answer;
        this.question = question;
    }

    @Override
    public String toString() {
        return answer;
    }
}
