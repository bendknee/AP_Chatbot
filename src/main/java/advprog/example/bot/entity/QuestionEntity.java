package advprog.example.bot.entity;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "question")
public class QuestionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private String question;

    @OneToMany(mappedBy = "question")
    private List<AnswerEntity> answers;

    @NotNull
    private String creatorId;

    @NotNull
    private int correctAnswerIndex;

    public QuestionEntity() {}

    public QuestionEntity(String creatorId) {
        this.creatorId = creatorId;
        this.answers = new ArrayList<>();
        this.correctAnswerIndex = -1;
    }

    public int getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public boolean isQuestionSet() { return this.question != null; }

    public void setQuestion(String question) { this.question = question; }

    public void addAnswer(AnswerEntity answer) {
        this.answers.add(answer);
    }

    public List<AnswerEntity> getAnswers() {
        return answers;
    }

    public boolean isAnswersFilled() {
        return this.answers.size() == 4;
    }

    public void setCorrectAnswerIndex(int correctAnswerIndex) {
        this.correctAnswerIndex = correctAnswerIndex;
    }

    public boolean hasCorrectAnswer() {
        return this.correctAnswerIndex != -1;
    }

    @Override
    public String toString() {
        String result = "pertanyaan:\n" + this.question + "\n\njawaban:\n";

        for (AnswerEntity answer: this.answers) {
            String answerString = answer.toString();

            result += answerString + "\n";
        }

        result += "\njawaban benar:\n" + this.answers.get(this.correctAnswerIndex).toString();

        return result;
    }
}
