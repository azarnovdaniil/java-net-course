package ru.gb.putilin.cloudstorage;

import ru.gb.putilin.cloudstorage.server.Server;
import ru.gb.putilin.cloudstorage.server.nio.StorageServer;

import java.io.IOException;

public class ServerApp {
    private static final int PORT = 8888;


    public static void main(String[] args) {
        try {
            Server ss = new Server();
            ss.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
