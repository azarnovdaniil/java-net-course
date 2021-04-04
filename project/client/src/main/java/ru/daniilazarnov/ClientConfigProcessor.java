package ru.daniilazarnov;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class ClientConfigProcessor {
    private String host;
    private int port;
    private String repoPath;
    private final Path configPath;
    private static final Logger LOGGER = LogManager.getLogger(ClientConfigProcessor.class);
    static final Marker toCon = MarkerManager.getMarker("CONS");

    ClientConfigProcessor(Path configPath) {
        this.configPath = configPath;
    }

    public void readConfig() {
        try {
            String[] temp = Files.readString(configPath).split("/");

            for (int i = 0; i < temp.length; i++) {
                if (temp[i].equals("host")) {
                    host = temp[i + 1];
                }
                if (temp[i].equals("port")) {
                    port = Integer.parseInt(temp[i + 1]);
                }
                if (temp[i].equals("repo")) {
                    Path repo = Paths.get(temp[i + 1]);
                    if (Files.notExists(repo)) {
                        repo.toFile().mkdir();
                    }
                    repoPath = repo.toString();
                }
            }
            LOGGER.info("Network settings initialized: " + host + " | " + port);
            System.out.println(String.format("Network settings initialized: host - %1$s, port - %2$d", host, port));
        } catch (IOException e) {
            LOGGER.error(toCon, "SWW reading config file.", LOGGER.throwing(e));
        }

    }

    public void writeConfig() {
        String settings = String.format("host/%1$s/port/%2$d/repo/%3$s/", host, port, repoPath);
        try {
            Files.writeString(configPath, settings, StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            LOGGER.error(toCon, "SWW writing to config file.", LOGGER.throwing(e));
        }
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getRepoPath() {
        return repoPath;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setRepoPath(String repoPath) {
        this.repoPath = repoPath;
    }
}
