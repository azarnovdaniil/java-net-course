package ru.daniilazarnov;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket server;
    private Socket socket;
    private final int PORT = 8189;

    public Server (){
        try {
            server = new ServerSocket(PORT);
            System.out.println("server started!");

            while (true) {
                socket = server.accept();
                System.out.println("client connected " + socket.getRemoteSocketAddress());
                new ClientHandler(this, socket);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("server closed");
            try {
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
