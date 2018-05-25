package advprog.example.bot.context;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.Mockito.mock;

import advprog.example.bot.entity.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class QuestionContextTest {
    private QuestionContext questionContext;

    @BeforeEach
    void setUp() {
        questionContext = new QuestionContext();
    }

    @Test
    void testHandlingContextAdd() {
        Question question = mock(Question.class);
        questionContext.putAddContext("CONTEXT", question);

        assertEquals(questionContext.getAddContext("CONTEXT"), question);
        assertTrue(questionContext.containsAddContextKey("CONTEXT"));

        questionContext.removeAddContext("CONTEXT");
        assertFalse(questionContext.containsAddContextKey("CONTEXT"));
    }

    @Test
    void testHandlingContextChange() {
        Question question = mock(Question.class);
        questionContext.putChangeContext("CONTEXT", question);

        assertEquals(questionContext.getChangeContext("CONTEXT"), question);
        assertTrue(questionContext.containsChangeContextKey("CONTEXT"));

        questionContext.removeChangeContext("CONTEXT");
        assertFalse(questionContext.containsChangeContextKey("CONTEXT"));
    }

}
