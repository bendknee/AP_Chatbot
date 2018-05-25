package advprog.example.bot.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

public class QuizTest {

    @Mock
    private Question question1;

    @Mock
    private Question question2;

    private Quiz quiz;

    @BeforeEach
    void setUp() {
        quiz = new Quiz(Arrays.asList((new Question[]{question1, question2})));
    }

    @Test
    void testGetAndIncrementUserScore() {
        String id = "1";

        quiz.incrementUserScore(id);
        assertTrue(quiz.getScores().get(id) == 1);
    }

    @Test
    void testGetRandomQuestion() {
        Quiz quiz = mock(Quiz.class);
        when(quiz.getRandomQuestion()).thenReturn(question1);

        assertEquals(quiz.getRandomQuestion(), question1);
    }

    @Test
    void testSetAndGetCurrentQuestion() {
        quiz.setCurrentQuestion(question1);

        assertEquals(quiz.getCurrentQuestion(), question1);
    }
}
