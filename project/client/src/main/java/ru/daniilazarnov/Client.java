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
    private static Selector selector;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }



    public static void start() throws IOException {
        String message = "";
//        new Thread(() -> {
//            try {
//                SelectionKey key = null;
//                while (FileProtocol.getClientSocketChannel().isOpen()) {
//                    Iterator<SelectionKey> iterator = selector.keys().iterator();
//                    while (iterator.hasNext()) {
//                        if (key.isReadable()) {
//                            FileProtocol.receiveBytesFromServer(key);
//                        }
//                    }
//                }
//            }
//            catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        }).start();

        FileProtocol.connect(new InetSocketAddress("localhost", 8199));
        while(!message.equals("exit")) {
            Scanner scanner = new Scanner(System.in);
            message = scanner.nextLine();
            FileProtocol.commandInterpretationFromClient(message);
//            Строки ниже требуются для упрощения отладки
//            Path path = Paths.get("project/client/src/main/resources/exceptions.png");
        }
    }
}
