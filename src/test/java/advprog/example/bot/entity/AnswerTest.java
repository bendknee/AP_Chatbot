package advprog.example.bot.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AnswerTest {

    private Answer answer;

    @BeforeEach
    public void setUp() {
        answer = new Answer("a!");
    }

    @Test
    public void testGetAnswer() {
        String answer = this.answer.getAnswer();

        assertThat(answer).isEqualTo("a!");
    }

    @Test
    public void testSetAnswer() {
        String newAnswer = "aa!";
        answer.setAnswer(newAnswer);

        assertThat(answer.getAnswer()).isEqualTo(newAnswer);
    }

    @Test
    public void testGetIsTrue() {
        answer.setCorrectAnswer(true);

        boolean isCorrect = answer.isCorrectAnswer();
        assertTrue(isCorrect);
    }

    @Test
    public void testSetIsTrue() {
        answer.setCorrectAnswer(false);
        assertFalse(answer.isCorrectAnswer());

        answer.setCorrectAnswer(true);
        assertTrue(answer.isCorrectAnswer());
    }
}
