package advprog.example.bot.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import javax.validation.constraints.NotNull;

@Entity
@Table(name = "question")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private String question;

    @OneToMany(mappedBy = "question", fetch = FetchType.EAGER)
    private List<Answer> answers;

    @NotNull
    private String creatorId;

    @NotNull
    private int correctAnswerIndex;

    public Question() {}

    public Question(String creatorId) {
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

    public boolean isQuestionSet() {
        return this.question != null;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void addAnswer(Answer answer) {
        this.answers.add(answer);
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public boolean isAnswersFilled() {
        return this.answers.size() == 4;
    }

    public void setCorrectAnswerIndex(int correctAnswerIndex) {
        this.correctAnswerIndex = correctAnswerIndex;
    }

    public Answer getCorrectAnswer() {
        return this.answers.get(this.correctAnswerIndex);
    }

    public boolean hasCorrectAnswer() {
        return this.correctAnswerIndex != -1;
    }

    @Override
    public String toString() {
        String result = "pertanyaan:\n" + this.question + "\n\njawaban:\n";

        for (Answer answer: this.answers) {
            String answerString = answer.toString();

            result += answerString + "\n";
        }

        result += "\njawaban benar:\n" + this.answers.get(this.correctAnswerIndex).toString();

        return result;
    }
}
