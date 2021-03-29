import org.junit.jupiter.api.Test;
import ru.daniilazarnov.MessageHandler;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class MessageHandlerTest {

    @Test
    void testConstructor() {
        MessageHandler mh = new MessageHandler(" -upload");
        assertEquals(mh.code, 1);
    }

    @Test
    void testHelp() {
        MessageHandler mh = new MessageHandler("-help");
        assertEquals(mh.code, -2);
    }

    @Test
    void testNotSupported() {
        MessageHandler mh = new MessageHandler("-heffflp");
        assertEquals(mh.code, -1);
    }
}
