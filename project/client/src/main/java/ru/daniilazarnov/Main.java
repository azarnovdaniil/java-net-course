package ru.daniilazarnov;

public class Main {
    public static void main(String[] args) {
        new Thread(()->{
            ClientApp clientApp = new ClientApp();
        }).start();
    }
}
