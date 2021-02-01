package ru.gb.putilin.cloudstorage;

import ru.gb.putilin.cloudstorage.client.Client;

import java.io.IOException;

public class ClientApp {

    private static final int PORT = 8888;
    private static final String PATH = "C:\\Projects\\SQL\\hw_putilin_l7\\2.PNG";

    public static void main(String[] args) {
        try {
            Client client = new Client(PORT);
            client.sendUri(PATH);
            if (client.readResponse()) {
                client.sendFile(PATH);
            }
            client.removeClient();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
