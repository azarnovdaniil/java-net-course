package ru.daniilazarnov;

import java.io.IOException;

public class ClientApp {

    public static void main(String[] args) {
        try {
            new Client().run(System.in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
