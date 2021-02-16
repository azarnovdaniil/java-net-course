package ru.daniilazarnov.client.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ClientConfiguration {
    private String downloads;

    public ClientConfiguration() {
        InputStream is = null;
        Properties property = new Properties();
        try {
            is = getClass().getClassLoader().getResourceAsStream("config.properties");
            property.load(is);
            downloads = property.getProperty("downloads");
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

    public String getDownloads() {
        return downloads;
    }
}
