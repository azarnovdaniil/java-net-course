package ru.daniilazarnov;

import java.io.*;
import java.net.URISyntaxException;

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
        String message = null;
        while(!message.equals("exit")) {
            Scanner scanner = new Scanner(System.in);
            message = scanner.nextLine();
            FileProtocol.trueSender(message);
//            Строки ниже требуются для упрощения отладки
//            Path path = Paths.get("project/client/src/main/resources/exceptions.png");
//            FileProtocol.connect(new InetSocketAddress(host, port));
        }
    }
}
