package ru.daniilazarnov.server.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ServerConfiguration {

    private String root;
    private String dbUrl;
    private String dbUser;
    private String dbPassword;

    public ServerConfiguration() {
        InputStream is = null;
        Properties property = new Properties();
        try {
            is = getClass().getClassLoader().getResourceAsStream("config.properties");
            property.load(is);
            root = property.getProperty("root");
            dbUrl = property.getProperty("dbUrl");
            dbUser = property.getProperty("dbUser");
            dbPassword = property.getProperty("dbPassword");
        } catch (IOException e) {
            System.err.println("Error has been occurred while reading client configuration!");
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

    public String getDbUrl() {
        return dbUrl;
    }

    public String getDbUser() {
        return dbUser;
    }

    public String getDbPassword() {
        return dbPassword;
    }
}
