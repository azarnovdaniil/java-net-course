package ru.daniilazarnov;

import java.net.ServerSocket;
import java.net.Socket;


public class ServerApp {

    public ServerApp() {
        start();
    }

    private void start() {
        try {
            ServerSocket serverSocket = new ServerSocket(8888);
            while (true) {
                System.out.println("!- Server is waiting for a connection.");
                Socket socket = serverSocket.accept();
                System.out.println("!- A new client connected.");
                new ClientHandler(socket);
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
