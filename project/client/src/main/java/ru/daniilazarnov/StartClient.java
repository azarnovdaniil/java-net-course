package ru.daniilazarnov;

import java.io.File;

public class StartClient {

    public static void main(String[] args) {
        Client client = new Client ();
        System.out.println("Ваше Облачное Хранилище!");
        client.startApp ();
    }
}
