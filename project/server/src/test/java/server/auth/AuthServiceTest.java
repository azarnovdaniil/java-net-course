package server.auth;

import org.junit.jupiter.api.*;
import ru.daniilazarnov.server.auth.AuthService;
import ru.daniilazarnov.server.auth.AuthenticationException;
import ru.daniilazarnov.server.database.sql.ConnectionService;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthServiceTest {

    private static final AuthService AUTH_SERVICE = new AuthService(
            new ru.daniilazarnov.server.database.sql.AuthService(
                    new ConnectionService("jdbc:mysql://localhost:3306/cs_auth",
                            "root",
                            "1")
            ));

    private static final String USER = "test";
    private static final String PASS = "123";
    private static final String SESSION_ID = "testSessionId";

    @Test
    @Order(1)
    void loginSuccessTest() {
        try {
            Assertions.assertTrue(AUTH_SERVICE.login(USER,PASS,SESSION_ID));
        } catch (AuthenticationException e) {
            e.printStackTrace();
        }
    }

    @Test
    void loginFailedTest() {
        try {
            Assertions.assertFalse(AUTH_SERVICE.login("123","123","123"));
        } catch (AuthenticationException e) {
            e.printStackTrace();
        }
    }

    @Test
    void checkSessionTest() {
        Assertions.assertTrue(AUTH_SERVICE.checkSession(SESSION_ID));
    }

    @Test
    void getUserBySessionIdTest() {
        Assertions.assertSame(USER, AUTH_SERVICE.getUserBySessionId(SESSION_ID));
    }
}