package ru.daniilazarnov;

public class Server {
    private static final int PORT = 8189;

    public static void main(String[] args) throws Exception {
        ServerInit serverApp = new ServerInit();
        serverApp.run(PORT);
    }
}