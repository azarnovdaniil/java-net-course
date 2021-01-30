package ru.daniilazarnov;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server {
    private ServerSocket server;
    private Socket socket;
    private final int PORT = 8189;
    private List<ClientHandler> clients;
//    private AuthService authService;

    public Server() {
        clients = new CopyOnWriteArrayList<>();

        try {
            server = new ServerSocket(PORT);
            System.out.println("server started!");

            while (true) {
                socket = server.accept();
                System.out.println("Клиент подключился " + socket.getRemoteSocketAddress());
                new ClientHandler(this, socket);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("Сервак закрылся");
            try {
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

//    public AuthService getAuthService() {
//        return authService;
//    }

}
