package ru.daniilazarnov;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class ClientConfigProcessor {
    private static final String DEFAULT_HOST = "127.0.0.1";
    private static final int DEFAULT_PORT = 7777;
    private static final String DEFAULT_REPO_PATH = "C:" + File.separator + "repo";
    private String host;
    private int port;
    private String repoPath;
    private final Path configPath;
    private static final Logger LOGGER = LogManager.getLogger(ClientConfigProcessor.class);
    static final Marker TO_CON = MarkerManager.getMarker("CONS");

    /**
     * Class reads App settings from the config file. If config file missing or invalid creates a new one
     * with default settings.
     *
     * @param configPath - path to config file.
     */

    ClientConfigProcessor(Path configPath) {
        this.configPath = configPath;
    }

    /**
     * Reads the config file. If it's impossible, inits default settings.
     */

    public void readConfig() {
        if (configPath.toFile().exists()) {
            try {
                String[] temp = Files.readString(configPath).split("&&&");
                boolean isHostSet = false;
                boolean isPortSet = false;
                boolean isRepoPathSet = false;

                for (int i = 0; i < temp.length; i++) {
                    if (temp[i].equals("host")) {
                        host = temp[i + 1];
                        isHostSet = true;
                    }
                    if (temp[i].equals("port")) {
                        port = Integer.parseInt(temp[i + 1]);
                        isPortSet = true;
                    }
                    if (temp[i].equals("repo")) {
                        Path repo = Paths.get(temp[i + 1]);
                        if (Files.notExists(repo)) {
                            repo.toFile().mkdir();
                        }
                        repoPath = repo.toString();
                        isRepoPathSet = true;
                    }
                }
                if (!isHostSet || !isPortSet || !isRepoPathSet) setDefaults();
                LOGGER.info("Network settings initialized: " + host + " | " + port);
                System.out.println(String.format("Network settings initialized: host - %1$s, port - %2$d", host, port));
                return;
            } catch (IOException e) {
                LOGGER.error(TO_CON, "SWW reading config file.", LOGGER.throwing(e));
            }
        }
        setDefaults();
    }

    /**
     * Writes actual settings to the config file.
     */

    public void writeConfig() {
        String settings = String.format("&&&host&&&%1$s&&&\n&&&port&&&%2$d&&&\n&&&repo&&&%3$s&&&",
                host, port, repoPath);
        try {
            if (!configPath.toFile().exists()) configPath.toFile().createNewFile();
            Files.writeString(configPath, settings, StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            LOGGER.error(TO_CON, "SWW writing to config file.", LOGGER.throwing(e));
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

    private void setDefaults() {
        this.host = DEFAULT_HOST;
        this.port = DEFAULT_PORT;
        this.repoPath = DEFAULT_REPO_PATH;
        writeConfig();
        LOGGER.info("Network settings initialized with default settings: " + host + " | " + port);
    }
}
