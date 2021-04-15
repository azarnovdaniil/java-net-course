package ru.daniilazarnov;

public class MainServer {
    public static void main(String[] args) throws Exception {

    new Server(Config.readConfig(Config.DEFAULT_CONFIG).getPort()).run();

    }
}
