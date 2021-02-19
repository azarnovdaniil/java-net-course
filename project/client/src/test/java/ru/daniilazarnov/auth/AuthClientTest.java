//package ru.daniilazarnov.auth;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import ru.daniilazarnov.network.Client;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class AuthClientTest {
//    AuthClient authClient = new AuthClient();
//    Client client = new Client();
//
//    @BeforeEach
//    void setUp() {
//        authClient.setAuthStatus(true);
//    }
//
//
//    @AfterEach
//    void tearDown() {
//        authClient.setAuthStatus(false);
//
//    }
//
//    @Test
//    void setAuthStatus() {
//
//    }
//
//
//    @Test
//    void getUserFolder() {
//    }
//
//    @Test
//    void isAuthStatus() {
//    }
//
//
//    @Test
//    void authentication() {
//
//    }
//
//    @Test
//    void getStringStatusAuth() {
//        System.out.println(authClient.getStringStatusAuth());
//        assertEquals("Регистрация подтверждена", authClient.getStringStatusAuth());
//    }
//
//    @Test
//    void auth() {
//    }
//}