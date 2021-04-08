package ru.daniilazarnov;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;


public class ServerConfigReader {
    private static final String DEFAULT_HOST = "127.0.0.1";
    private static final int DEFAULT_PORT = 7777;
    private static final String DEFAULT_REPO_PATH = "C:" + File.separator + "cloudrepo";
    private static final String[] DEFAULT_DB_PARAM = new String[]{"jdbc:mysql://localhost:3306/repouserbase", "root", "1234"};
    private static int port;
    private static String host;
    private static String repoPath;
    private static final Path configPath = Paths.get("ServerConfig.cfg");
    private static final String[] dBparam = new String[3];
    private static final Logger LOGGER = LogManager.getLogger(ServerConfigReader.class);

    /**
     * Reads server settings from config file. If config file is absent or invalid, sets default settings
     * and creates a new config file.
     */

    public static void readConfig() {
        if (configPath.toFile().exists()) {
            HashMap<String, Boolean> checklist = new HashMap<>();
            checklist.put("host", false);
            checklist.put("port", false);
            checklist.put("repo", false);
            checklist.put("DBpath", false);
            checklist.put("DBuser", false);
            checklist.put("DBpass", false);

            try {
                String[] temp = Files.readString(configPath).split("&&&");

                for (int i = 0; i < temp.length; i++) {
                    if (temp[i].equals("host")) {
                        host = temp[i + 1];
                        checklist.replace("host", true);
                    }
                    if (temp[i].equals("port")) {
                        port = Integer.parseInt(temp[i + 1]);
                        checklist.replace("port", true);
                    }
                    if (temp[i].equals("repo")) {
                        Path repo = Paths.get(temp[i + 1]);
                        if (Files.notExists(repo)) {
                            repo.toFile().mkdir();
                        }
                        repoPath = repo.toString();
                        checklist.replace("repo", true);
                    }
                    if (temp[i].equals("DBpath")) {
                        dBparam[0] = temp[i + 1];
                        checklist.replace("DBpath", true);
                    }
                    if (temp[i].equals("DBuser")) {
                        dBparam[1] = temp[i + 1];
                        checklist.replace("DBuser", true);
                    }
                    if (temp[i].equals("DBpass")) {
                        dBparam[2] = temp[i + 1];
                        checklist.replace("DBpass", true);
                    }
                }
                if (checklist.values().stream().anyMatch(v -> !v)) {
                    setDefaults();
                    return;
                }
                LOGGER.info(String.format("Network settings initialized: host - %1$s, port - %2$d", host, port));
            } catch (IOException e) {
                LOGGER.error("SWW reading config!", LOGGER.throwing(e));
            }
        } else setDefaults();

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

    private static void setDefaults() {

        String settings = String.format(
                "&&&host&&&%1$s&&&\n&&&port&&&%2$d&&&\n&&&repo&&&%3$s&&&\n&&&DBpath&&&%4$s&&&\n&&&DBuser&&&%5$s&&&\n&&&DBpass&&&%6$s&&&",
                DEFAULT_HOST, DEFAULT_PORT, DEFAULT_REPO_PATH, DEFAULT_DB_PARAM[0], DEFAULT_DB_PARAM[1], DEFAULT_DB_PARAM[2]);
        try {
            if (!configPath.toFile().exists()) configPath.toFile().createNewFile();
            Files.writeString(configPath, settings, StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING);
            readConfig();
        } catch (IOException e) {
            LOGGER.error("SWW writing to config file.", LOGGER.throwing(e));
        }
    }
}
