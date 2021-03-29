package ru.daniilazarnov;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URISyntaxException;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Scanner;

class Main2 {
    public static void main(String[] args) throws IOException, URISyntaxException {
        Client.start();
    }
}


public class Client {

    private final int port;
    private final String host;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }



    public static void start() throws IOException {
        String message = "";
        FileProtocol.connect(new InetSocketAddress("localhost", 8199));
        while(!message.equals("exit")) {
            Scanner scanner = new Scanner(System.in);
            message = scanner.nextLine();
            FileProtocol.commandInterpretationFromClient(message);
        }
    }
}
