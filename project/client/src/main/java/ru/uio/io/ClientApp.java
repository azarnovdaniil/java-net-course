package ru.uio.io;

import java.io.IOException;

public class ClientApp {
    public static void main(String[] args) {
        try {
            new Client().start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
