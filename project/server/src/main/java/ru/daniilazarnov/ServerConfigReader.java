package ru.daniilazarnov;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ServerConfigReader {
    private static int port;
    private static String host;
    private static String repoPath;
    private static final Path configPath = Paths.get("server", "src", "main", "resources", "config.cfg");
    private static final String[] dBparam = new String[3];
    private static final Logger LOGGER = LogManager.getLogger(ServerConfigReader.class);


    public static void readConfig() {
        try {
            String[] temp = Files.readString(configPath).split("&&&");

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
                if (temp[i].equals("DBpath")) {
                    dBparam[0] = temp[i + 1];
                }
                if (temp[i].equals("DBuser")) {
                    dBparam[1] = temp[i + 1];
                }
                if (temp[i].equals("DBpass")) {
                    dBparam[2] = temp[i + 1];
                }
            }
            LOGGER.info(String.format("Network settings initialized: host - %1$s, port - %2$d", host, port));
        } catch (IOException e) {
            LOGGER.error("SWW reading config!", LOGGER.throwing(e));
        }

    }

    public static int getPort() {
        return port;
    }

    public static String getHost() {
        return host;
    }

    public static String getRepoPath() {
        return repoPath;
    }

    public static String[] getDBparam() {
        return dBparam;
    }

    public static void checkDir(String dirName) {
        Path dir = Paths.get(dirName);
        if (!dir.toFile().exists() || !dir.toFile().isDirectory()) {
            dir.toFile().mkdir();
        }
    }
}
