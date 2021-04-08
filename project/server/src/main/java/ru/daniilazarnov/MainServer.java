package ru.daniilazarnov;

public class MainServer {
    public static void main(String[] args) throws Exception {
        new Server(Common.DEFAULT_PORT).run();
    }
}
