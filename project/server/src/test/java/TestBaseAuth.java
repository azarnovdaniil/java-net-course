import org.junit.jupiter.api.Test;
import ru.atoroschin.Credentials;
import ru.atoroschin.auth.AuthService;
import ru.atoroschin.auth.BaseAuthService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestBaseAuth {
    private AuthService authService = new BaseAuthService();

    @Test
    void TestAuth() {
        assertTrue(authService.isAuth(new Credentials(List.of("user1", "1"))));
        assertFalse(authService.isAuth(new Credentials(List.of("user1", "2"))));
        assertFalse(authService.isAuth(new Credentials(List.of("user", "1"))));
    }

    @Test
    void TestFolder() throws IllegalAccessException {
        assertEquals("user_1", authService.getUserFolder("user1"));
        assertThrows(IllegalAccessException.class, () -> authService.getUserFolder("user"));
        assertDoesNotThrow(() -> authService.getUserFolder("user1"));
    }

    @Test
    void TestMaxVolume() throws IllegalAccessException {
        assertEquals(1, authService.getMaxVolume("user1"));
        assertThrows(IllegalAccessException.class, () -> authService.getMaxVolume("user"));
        assertDoesNotThrow(() -> authService.getMaxVolume("user1"));
    }
}
