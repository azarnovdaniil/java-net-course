import org.junit.jupiter.api.Test;
import ru.daniilazarnov.MessageHandler;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class MessageHandlerTest {

    @Test
    void testConstructor() {
        MessageHandler mh = new MessageHandler(" -upload test.pdf reff fdsfsdf");
        System.out.println(mh.arguments[0]);
        assertEquals(mh.code, 1);
    }

    @Test
    void testHelp() {
        MessageHandler mh = new MessageHandler("-help");
        assertEquals(mh.code, -2);
    }

    @Test
    void testNotSupported() {
        MessageHandler mh = new MessageHandler("-heffflp  gfhdfhgfh");
        assertEquals(mh.code, -1);
    }

    @Test
    void testUploadFile() {
        String path = "D:\\JavaProjects\\java-net-course\\project\\client\\src\\main\\java\\ru\\daniilazarnov\\test.txt";
        MessageHandler mh = new MessageHandler("-upload " + path);
    }

}
