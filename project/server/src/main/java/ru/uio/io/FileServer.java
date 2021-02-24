package ru.uio.io;

import ru.uio.io.auth.AuthenticationService;
import ru.uio.io.auth.BasicAuthenticationService;
import ru.uio.io.sql.JdbcSQLiteConnection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

public class FileServer implements Server {
    private Set<ClientHandler> clients;
    private JdbcSQLiteConnection sqLiteConnection;
    private AuthenticationService authenticationService;

    public FileServer() {
        try {
            System.out.println("Server is starting up...");
            ServerSocket serverSocket = new ServerSocket(8888);
            clients = new HashSet<>();
            sqLiteConnection = new JdbcSQLiteConnection();
            authenticationService = new BasicAuthenticationService(sqLiteConnection);
            System.out.println("Server is started up...");
            Path dir = Paths.get("store");
            try {
                if(!Files.isDirectory(dir))
                    Files.createDirectory(dir);
            } catch (IOException e) {
                e.printStackTrace();
            }

            while (true) {
                System.out.println("Server is listening for clients...");
                Socket socket = serverSocket.accept();
                System.out.println("Client accepted: " + socket);
                new ClientHandler(this, socket);
            }
        } catch (IOException e) {
            throw new RuntimeException("SWW5", e);
        } finally {
            authenticationService.close();
        }

    }

    @Override
    public synchronized void broadcastMessage(String message) {
        clients.forEach(client -> client.sendMessage(message));
    }

    @Override
    public synchronized boolean isLoggedIn(String nickname) {
        return clients.stream()
                .filter(clientHandler -> clientHandler.getName().equals(nickname))
                .findFirst()
                .isPresent();
    }

    @Override
    public synchronized void subscribe(ClientHandler client) {
        clients.add(client);
    }

    @Override
    public synchronized void unsubscribe(ClientHandler client) {
        clients.remove(client);
    }

    @Override
    public AuthenticationService getAuthenticationService() {
        return authenticationService;
    }

    @Override
    public JdbcSQLiteConnection getConnect(){
        return this.sqLiteConnection;
    }
}