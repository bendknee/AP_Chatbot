package advprog.example.bot.entity;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class QuestionTest {

    private Question question;
    private Answer answer1;
    private Answer answer2;

    @BeforeEach
    void setUp() {
        question = new Question("?");
    }

    @Test
    void testGetQuestion() {
        String question = this.question.getQuestion();

        assertThat(question).isEqualTo("?");
    }

    @Test
    void testSetQuestion() {
        String newQuestion = "??";
        question.setQuestion(newQuestion);

        assertThat(newQuestion).isEqualTo(newQuestion);
    }

    @Test
    void testAddAnswers() {
        question.addAnswer(answer1);
        question.addAnswer(answer2);

        assertThat(question.getAnswers().size()).isEqualTo(2);
    }
}
