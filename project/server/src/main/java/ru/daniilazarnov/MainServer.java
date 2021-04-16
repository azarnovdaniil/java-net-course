package ru.daniilazarnov;

public class MainServer {
    public static void main(String[] args) throws Exception {
        Config config = Config.readConfig(Config.DEFAULT_CONFIG);
        new Server(config.getPort()).run();
    }
}
