//package advprog.example.bot.handler;
//
//import com.linecorp.bot.model.message.TextMessage;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//public class QuizHandlerTest {
//
//    private QuizHandler zonkComposer;
//
//    @BeforeEach
//    void setUp() {
//        zonkComposer = mock(QuizHandler.class);
//
//        when(zonkComposer.composeReply("zonk")).thenReturn(new TextMessage("zonk!"));
//    }
//
//    @Test
//    void testComposeZonkMessage() {
//        TextMessage zonkMessage = new TextMessage("zonk!");
//
//        assertThat(zonkComposer.composeReply("zonk")).isEqualTo(zonkMessage);
//    }
//}
