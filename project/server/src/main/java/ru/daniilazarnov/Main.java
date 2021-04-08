package ru.daniilazarnov;

public class Main {
    public static void main(String[] args) {
        try {
            NetStorageServer netStorageServer = new NetStorageServer();
            netStorageServer.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}