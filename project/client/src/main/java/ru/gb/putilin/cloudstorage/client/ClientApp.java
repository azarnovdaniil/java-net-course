package ru.gb.putilin.cloudstorage.client;

import ru.gb.putilin.cloudstorage.client.network.ClientConnection;
import ru.gb.putilin.cloudstorage.client.network.FilesService;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ClientApp {

    private static final int PORT = 8888;
    private static final String HOST = "localhost";

    public static void main(String[] args) {
        ClientConnection clientConnection = new ClientConnection(HOST, PORT);
        clientConnection.start(new CountDownLatch(1));
        clientConnection.stop();



    }


}
