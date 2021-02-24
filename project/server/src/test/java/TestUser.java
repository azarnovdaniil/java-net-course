import org.junit.jupiter.api.Test;
import ru.atoroschin.Credentials;
import ru.atoroschin.auth.User;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class TestUser {
    private final User user = new User("user","1","111", 1);

    @Test
    void TestAuth() {
        assertTrue(user.isAuth(new Credentials(List.of("user", "1"))));
        assertFalse(user.isAuth(new Credentials(List.of("user", "2"))));
        assertFalse(user.isAuth(new Credentials(List.of("user2", "1"))));
    }

    @Test
    void TestLogin() {
        assertEquals("user", user.getLogin());
        assertNotEquals("user2", user.getLogin());
    }

    @Test
    void TestFolder() {
        assertEquals("111", user.getFolder());
        assertNotEquals("222", user.getFolder());
    }

    @Test
    void TestMaxVolume() {
        assertEquals(1, user.getMaxVolume());
        assertNotEquals(222, user.getMaxVolume());
    }

}
