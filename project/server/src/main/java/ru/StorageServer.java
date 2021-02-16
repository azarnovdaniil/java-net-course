package ru;


public class StorageServer {

    private final int PORT = 60111;

    public void run() {
        new NettyServer(PORT).run();
    }


}