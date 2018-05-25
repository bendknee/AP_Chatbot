package advprog.example.bot.context;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.Mockito.mock;

import advprog.example.bot.entity.Question;
import advprog.example.bot.entity.Quiz;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class QuizContextTest {
    private QuizContext quizContext;

    @BeforeEach
    void setUp() {
        quizContext = new QuizContext();
    }

    @Test
    void testHandlingContextAdd() {
        Quiz quiz = mock(Quiz.class);
        quizContext.putContext("CONTEXT", quiz);

        assertEquals(quizContext.getContext("CONTEXT"), quiz);
        assertTrue(quizContext.containsContext("CONTEXT"));

        quizContext.removeContext("CONTEXT");
        assertFalse(quizContext.containsContext("CONTEXT"));
    }
}
