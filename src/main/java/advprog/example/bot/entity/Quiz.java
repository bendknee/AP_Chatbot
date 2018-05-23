package advprog.example.bot.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Quiz {
    private HashMap<String, Integer> scores = new HashMap<>();
    private List<Question> questions;
    private Question currentQuestion;
    private Random random = new Random();

    public Quiz(List<Question> questions) {
        this.questions = questions;
    }

    public void incrementUserScore(String id) {
        int nextScore = scores.getOrDefault(id, 0) + 1;
        System.out.println(id + " " + nextScore);
        scores.put(id, nextScore);
    }

    public HashMap<String, Integer> getScores() {
        return scores;
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
