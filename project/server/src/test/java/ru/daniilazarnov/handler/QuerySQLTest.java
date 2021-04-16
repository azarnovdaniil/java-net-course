package ru.daniilazarnov.handler;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.daniilazarnov.handler.sql.ConnectionService;
import ru.daniilazarnov.handler.sql.QuerySQL;

class QuerySQLTest {
    private QuerySQL query;
    @BeforeEach
    public void init() {
        query = new QuerySQL();
    }
    @Test
    void tryAuthInStorage() {
        String login1 = "login1";
        String pass1 = "pass1";
        Assertions.assertTrue(query.tryAuthInStorage(ConnectionService.connectMySQL(), login1, pass1));
        Assertions.assertFalse(query.tryAuthInStorage(ConnectionService.connectMySQL(), login1, login1));
    }

    @Test
    void tryToRegistNewUser() {
        String login1 = "login50";
        String pass1 = "pass50";
        String login2 = "";
        Assertions.assertTrue(query.tryToRegistNewUser(ConnectionService.connectMySQL(), login1, pass1));
        query.deleteFromDb(ConnectionService.connectMySQL(),login1);
    }

    @Test
    void isLoginInDb() {
        String login1 = "login1";
        String login2 = "login10";
        Assertions.assertTrue(query.isLoginInDb(ConnectionService.connectMySQL(), login1));
        Assertions.assertFalse(query.isLoginInDb(ConnectionService.connectMySQL(), login2));
    }


    @Test
    void deleteFromDb() {
        String login1 = "login50";
        String pass1 = "pass50";
        query.tryToRegistNewUser(ConnectionService.connectMySQL(), login1, pass1);
        Assertions.assertTrue(query.deleteFromDb(ConnectionService.connectMySQL(), login1));
    }
}

