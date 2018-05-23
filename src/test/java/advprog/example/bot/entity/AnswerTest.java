package advprog.example.bot.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AnswerTest {

    private Answer answer;

    @BeforeEach
    public void setUp() {
        answer = new Answer("a!", null);
    }

    @Test
    public void testDefaultConstructor() {
        Answer answer = new Answer();
        assertNotNull(answer);
    }

    @Test
    public void testGetAnswer() {
        String answer = this.answer.toString();

        assertThat(answer).isEqualTo("a!");
    }

}
