package ru.daniilazarnov;

public class StartServer {
    public static void main(String[] args) {
        try {
            new Server().run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
