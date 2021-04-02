package ru.daniilazarnov;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Server {

    private static int port;
    private static String host;
    private static String repoPath;
    private static final Path configPath = Paths.get("server\\src\\main\\java\\ru\\daniilazarnov\\resources\\config.cfg");
    private static final String [] DBparam = new String[3];

    public static void main(String[] args) throws IOException, InterruptedException {
        readConfig();
        RepoServer server = new RepoServer(host, port);
        server.start();

    }

    private static void readConfig() {
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
                    repoPath = temp[i + 1];
                }
                if (temp[i].equals("DBpath")) {
                    DBparam[0] = temp[i + 1];
                }
                if (temp[i].equals("DBuser")) {
                    DBparam[1] = temp[i + 1];
                }
                if (temp[i].equals("DBpass")) {
                    DBparam[2] = temp[i + 1];
                }
            }
            System.out.println(String.format("Network settings initialized: host - %1$s, port - %2$d", host, port));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public static String getRepoPath() {
        return repoPath;
    }

    public static String[] getDBparam(){
        return DBparam;
    }

    public static void checkDir(String dirName){
        Path dir = Paths.get(dirName);
        if (!dir.toFile().exists()||!dir.toFile().isDirectory()){
            dir.toFile().mkdir();
        }
    }
}
