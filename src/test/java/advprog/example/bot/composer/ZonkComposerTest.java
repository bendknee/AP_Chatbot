package advprog.example.bot.composer;

import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.soap.Text;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ZonkComposerTest {

    private ZonkComposer zonkComposer;

    @BeforeEach
    void setUp() {
        zonkComposer = mock(ZonkComposer.class);

        when(zonkComposer.composeMessage("zonk")).thenReturn(new TextMessage("zonk!"));
    }

    @Test
    void testComposeZonkMessage() {
        TextMessage zonkMessage = new TextMessage("zonk!");

        assertThat(zonkComposer.composeMessage("zonk")).isEqualTo(zonkMessage);
    }
}
