package advprog.example.bot.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class QuestionTest {

    private Question question;
    private Answer answer1;
    private Answer answer2;

    @BeforeEach
    void setUp() {
        question = mock(Question.class);
        answer1 = mock(Answer.class);
        answer2 = mock(Answer.class);
    }

    @Test
    void testConstructors() {
        Question question = new Question();
        Question question1 = new Question("DUMMY_ID");

        assertNotNull(question);
        assertNotNull(question1);
    }

    @Test
    void testGetId() {
        when(question.getId()).thenReturn(1);

        assertEquals(1, question.getId());
    }

    @Test
    void testGetQuestion() {
        when(question.getQuestion()).thenReturn("?");

        assertEquals("?", question.getQuestion());
    }

    @Test
    void testIsQuestionSet() {
        when(question.isQuestionSet()).thenReturn(true);

        assertTrue(question.isQuestionSet());
    }

    @Test
    void testSetQuestion() {
        String newQuestion = "??";
        question.setQuestion(newQuestion);

        assertThat(newQuestion).isEqualTo(newQuestion);
    }

    @Test
    void testAddAnswers() {
        when(question.getAnswers()).thenReturn(new ArrayList<>());

        assertThat(question.getAnswers().size()).isEqualTo(0);
    }

    @Test
    void testGetCorrectAnswer() {
        when(question.getCorrectAnswer()).thenReturn(answer1);

        assertEquals(question.getCorrectAnswer(), answer1);
    }

    @Test
    void testHasCorrectAnswer() {
        when(question.hasCorrectAnswer()).thenReturn(true);

        assertTrue(question.hasCorrectAnswer());
    }
}
