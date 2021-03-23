package ru.daniilazarnov;

import java.io.IOException;

public class MainServer {
    public static void main(String[] args) throws IOException {
        new Thread(new Server()).start();
    }
}
