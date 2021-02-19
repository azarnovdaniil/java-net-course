package ru.daniilazarnov.client;

import ru.daniilazarnov.client.configuration.ClientConfiguration;
import ru.daniilazarnov.client.network.ClientConnection;

import java.util.concurrent.CountDownLatch;

public class ClientApp2 {

    private static final int PORT = 8888;
    private static final String HOST = "localhost";

    public static void main(String[] args) {
        ClientConnection clientConnection = new ClientConnection(HOST, PORT, new ClientConfiguration());
        clientConnection.start(new CountDownLatch(1));
        clientConnection.stop();
    }
}
