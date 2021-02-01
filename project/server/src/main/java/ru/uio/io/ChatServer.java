package ru.uio.io;

import ru.uio.io.auth.AuthenticationService;
import ru.uio.io.auth.BasicAuthenticationService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class ChatServer implements Server {
    private Set<ClientHandler> clients;
    private AuthenticationService authenticationService;

    public ChatServer() {
        try {
            System.out.println("Server is starting up...");
            ServerSocket serverSocket = new ServerSocket(8888);
            clients = new HashSet<>();
            authenticationService = new BasicAuthenticationService();
            System.out.println("Server is started up...");

            while (true) {
                System.out.println("Server is listening for clients...");
                Socket socket = serverSocket.accept();
                System.out.println("Client accepted: " + socket);
                new ClientHandler(this, socket);
            }
        } catch (IOException e) {
            throw new RuntimeException("SWW5", e);
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

    /**
     * Отправляет сообщение только слиенту с именем clientName
     * @param message Сообщение
     * @param clientName Клиент которому адресованно сообщение
     * @return false - если пользователя нет , true - если отправленно
     */
    @Override
    public boolean whisper(String message, String clientName) {
        if (!isLoggedIn(clientName)){
            return false;
        }
        for (ClientHandler client : clients) {
            if (client.getName().equals(clientName)){
                client.sendMessage(message);
                return true;
            }
        }
        return false;
    }
}
