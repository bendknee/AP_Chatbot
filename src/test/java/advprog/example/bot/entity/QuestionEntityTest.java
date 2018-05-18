package advprog.example.bot.entity;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class QuestionEntityTest {

    private QuestionEntity questionEntity;
    private AnswerEntity answerEntity1;
    private AnswerEntity answerEntity2;

    @BeforeEach
    void setUp() {
        questionEntity = new QuestionEntity("?");
    }

    @Test
    void testGetQuestion() {
        String question = questionEntity.getQuestion();

        assertThat(question).isEqualTo("?");
    }

    @Test
    void testSetQuestion() {
        String newQuestion = "??";
        questionEntity.setQuestion(newQuestion);

        assertThat(newQuestion).isEqualTo(newQuestion);
    }

    @Test
    void testAddAnswers() {
        questionEntity.addAnswer(answerEntity1);
        questionEntity.addAnswer(answerEntity2);

        assertThat(questionEntity.getAnswers().size()).isEqualTo(2);
    }
}
