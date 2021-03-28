package ru.daniilazarnov;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.function.Consumer;


public class Client {
    private static int port;
    private static String host;
    private static String repoPath;
    private static final Path configPath = Paths.get("client\\src\\main\\resources\\config.cfg");
    private static boolean isAuthorised = false;

    public static void main(String[] args) throws IOException, InterruptedException {
        readConfig();
        RepoClient client = new RepoClient(host, port,repoPath);
        Console console = new Console(client, new Consumer<ContextData>() {
            @Override
            public void accept(ContextData contextData) {
                client.execute(contextData);
            }
        });
        client.start();
        writeConfig();
        console.start();

    }

    private static void readConfig(){
        try {
            String [] temp = Files.readString(configPath).split("/");

            for (int i = 0; i < temp.length; i++) {
                if (temp[i].equals("host")){
                    host=temp[i+1];
                }
                if (temp[i].equals("port")) {
                    port = Integer.parseInt(temp[i+1]);
                }
                if (temp[i].equals("repo")){
                    Path repo = Paths.get(temp[i+1]);
                    if (Files.notExists(repo)){
                        repo.toFile().mkdir();
                    }
                    repoPath = temp[i+1];
                }
            }
            System.out.println(String.format("Network settings initialized: host - %1$s, port - %2$d", host, port));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void writeConfig () {
        String settings = String.format("host/%1$s/port/%2$d/repo/%3$s/",host,port,repoPath);
        try {
            Files.writeString(configPath,settings, StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setPort(int port) {
        Client.port = port;
    }

    public static void setHost(String host) {
        Client.host = host;
    }

    public static void setRepoPath(String repoPath) {
        Client.repoPath = repoPath;
    }

    public static void setIsAuthorised(boolean isAuthorised) {
        Client.isAuthorised = isAuthorised;
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

    public static boolean isIsAuthorised() {
        return isAuthorised;
    }
}
