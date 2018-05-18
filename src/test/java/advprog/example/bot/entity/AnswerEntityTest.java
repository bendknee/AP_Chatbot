package advprog.example.bot.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AnswerEntityTest {

    private AnswerEntity answerEntity;

    @BeforeEach
    public void setUp() {
        answerEntity = new AnswerEntity("a!");
    }

    @Test
    public void testGetAnswer() {
        String answer = answerEntity.getAnswer();

        assertThat(answer).isEqualTo("a!");
    }

    @Test
    public void testSetAnswer() {
        String newAnswer = "aa!";
        answerEntity.setAnswer(newAnswer);

        assertThat(answerEntity.getAnswer()).isEqualTo(newAnswer);
    }

    @Test
    public void testGetIsTrue() {
        answerEntity.setCorrectAnswer(true);

        boolean isCorrect = answerEntity.isCorrectAnswer();
        assertTrue(isCorrect);
    }

    @Test
    public void testSetIsTrue() {
        answerEntity.setCorrectAnswer(false);
        assertFalse(answerEntity.isCorrectAnswer());

        answerEntity.setCorrectAnswer(true);
        assertTrue(answerEntity.isCorrectAnswer());
    }
}
