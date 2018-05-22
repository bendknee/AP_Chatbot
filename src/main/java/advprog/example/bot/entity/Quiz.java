package advprog.example.bot.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Quiz {
    private HashMap<String, Integer> score = new HashMap<>();
    private List<Question> questions;
    private Question currentQuestion;
    private Random random = new Random();

    public Quiz(List<Question> questions) {
        this.questions = questions;
    }

    public void incrementUserScore(String id) {
        score.put(id, score.get(id) + 1);
    }

    public HashMap<String, Integer> getScore() {
        return score;
    }

    public Question getRandomQuestion() {
        Question question = null;

        if (this.questions.size() > 0) {
            question = questions.get(random.nextInt(questions.size()));
        }

        return question;
    }

    public Question getCurrentQuestion() {
        return currentQuestion;
    }

    public void setCurrentQuestion(Question currentQuestion) {
        this.currentQuestion = currentQuestion;
    }
}
