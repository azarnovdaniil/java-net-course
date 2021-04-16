package ru.daniilazarnov;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;



import java.io.IOException;

class ConfigTest {

    @BeforeEach
    public void init() {
        Config config = new Config();
    }

    @Test
    void readConfig () throws IOException {
        // Файлы конфигурации
        String cnf = "config.txt";
        String cnf1 = "config1.txt";
        String cnf2 = "config2.txt";
        String cnf3 = "config3.txt";
        // получаемые параметры
        String host = "localhost";
        int port = 8189;

        Assert.assertEquals(port, Config.readConfig(cnf).getPort());
        Assert.assertEquals(host, Config.readConfig(cnf).getHost());
        Assert.assertNotNull(Config.readConfig(cnf));

        Assert.assertNull(Config.readConfig(cnf1));

        Assert.assertNull(Config.readConfig(cnf2));

        Assert.assertNull(Config.readConfig(cnf3));
    }
}