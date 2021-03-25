package ru.daniilazarnov;

public class ClientApp {
    public static void main(String[] args) throws InterruptedException {
        new Client("localhost", 8080).start();
    }
}
