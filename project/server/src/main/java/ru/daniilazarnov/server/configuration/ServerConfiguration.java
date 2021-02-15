package ru.daniilazarnov.server.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ServerConfiguration {

    private String root;

    public ServerConfiguration() {
        InputStream is = null;
        Properties property = new Properties();
        try {
            is = getClass().getClassLoader().getResourceAsStream("config.properties");
            property.load(is);
            root = property.getProperty("root");
        } catch (IOException e) {
            System.err.println("Ошибка чтения конфигурационного файла!");
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getRoot() {
        return root;
    }
}
