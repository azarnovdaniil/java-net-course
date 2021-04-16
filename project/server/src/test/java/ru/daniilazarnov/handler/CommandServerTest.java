package ru.daniilazarnov.handler;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.daniilazarnov.Config;
import ru.daniilazarnov.handler.sql.ConnectionService;
import ru.daniilazarnov.handler.sql.QuerySQL;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

class CommandServerTest {
    private CommandServer cmd;
    @BeforeEach
    public void init() {
        cmd = new CommandServer();
    }
    @Test
    void authInStorage() {
        String user1 = "login1";
        String pass1 = "pass1";
        String exceptedGood = "Wellcome " + user1;
        String exceptedBad = "Wrong username/password";
        Assert.assertEquals(exceptedGood, cmd.authInStorage(user1, pass1));
        Assert.assertEquals(exceptedBad, cmd.authInStorage(user1, user1));
    }

    @Test
    void regInStorage() throws IOException {
        Config config = Config.readConfig(Config.DEFAULT_CONFIG);
        String user1 = "login10";
        String pass1 = "pass10";
        String repo = config.getServerRepo();
        String exceptedGood = "Registration in the system is completed";
        String exceptedRedudance = "User " + user1 + " is already exist";

        Assert.assertEquals(exceptedGood, cmd.regInStorage(user1, pass1, repo));
        Assert.assertEquals(exceptedRedudance, cmd.regInStorage(user1, pass1, repo));
        (new QuerySQL()).deleteFromDb(ConnectionService.connectMySQL(), user1);
    }

    @Test
    void callHelpManual() throws IOException {
        StringBuilder file = cmd.callHelpManual();
        String[] strings = file.toString().split("\n\r");
        String begin = "/help - calling help";
        String end = "/fin -out from program";
        Assert.assertNotNull(cmd.callHelpManual());
        Assert.assertEquals(begin, strings[0]);
        Assert.assertEquals(end, strings[strings.length-1]);
    }
}