package helpers;

import java.io.*;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Properties;

public class ConfigHelper {

    private final Path configPath;
    private final Properties properties;

    public ConfigHelper(Path configPath) throws IOException {
        this.configPath = configPath;
        this.properties = new Properties();
        load();
    }

    public void create(Map<String, String> settings) throws IOException {
        try (OutputStream os = new FileOutputStream(configPath.toString())) {
            properties.putAll(settings);
            properties.store(os, LocalDateTime.now().toString());
        }
    }

    public void load() throws IOException {
        try (InputStream is = new FileInputStream(configPath.toString())) {
            properties.load(is);
        }
    }

    public void save() throws IOException {
        try (OutputStream os = new FileOutputStream(configPath.toString())) {
            properties.store(os, LocalDateTime.now().toString());
        }
    }

    public String getProperty(String name) {
        return properties.getProperty(name);
    }

    public void setProperty(String name, String value) {
        properties.setProperty(name, value);
    }
}
