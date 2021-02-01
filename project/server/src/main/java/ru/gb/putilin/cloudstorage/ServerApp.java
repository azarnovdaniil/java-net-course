package ru.gb.putilin.cloudstorage;

import ru.gb.putilin.cloudstorage.server.StorageServer;

import java.io.IOException;

public class ServerApp {
    private static final int PORT = 8888;


    public static void main(String[] args) {
        try {
            StorageServer ss = new StorageServer(PORT);
            ss.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
