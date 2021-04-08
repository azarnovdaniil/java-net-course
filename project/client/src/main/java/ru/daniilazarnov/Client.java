package ru.daniilazarnov;

import java.io.IOException;

public class Client {
    private static final int PORT = 8189;
    private static final String HOST = "localhost";

    public static void main(String[] args) throws IOException {
        NetworkService networkService = new NetworkService();
        CommandHandler handler = new CommandHandler(networkService);
        networkService.run(HOST, PORT);
        handler.sendCommand();

    }
}