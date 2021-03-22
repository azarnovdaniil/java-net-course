package chat.server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerChat implements Chat {
    private ServerSocket serverSocket;
    private Set<ClientHandler> clients;
    private AuthenticationService authenticationService;
    private Logger logger = Logger.getLogger(this.getClass().getName());

    public ServerChat() {
        start();
    }

    @Override
    public AuthenticationService getAuthenticationService() {
        return authenticationService;
    }

    private void start() {
        try {
            serverSocket = new ServerSocket(8888);
            clients = new HashSet<>();
            authenticationService = new AuthenticationService();
            logger.log(Level.INFO, "Server is starting");

            while (true) {
                System.out.println("Server is waiting for a connection ...");
                Socket socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(socket, this);
                System.out.printf("[%s] Client[%s] is successfully logged in%n", new Date(), clientHandler.getName());
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "Server cant start");
            e.printStackTrace();
        }
    }


    @Override
    public synchronized void broadcastMessage(String message) {
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
        logger.log(Level.FINE, "broadcast message was sent");
    }

    @Override
    public synchronized boolean isNicknameOccupied(String nickname) {
        for (ClientHandler client : clients) {
            if (client.getName().equals(nickname)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public synchronized void subscribe(ClientHandler client) {
        clients.add(client);
        logger.log(Level.INFO, "new client is subscribing - " + client.getName());
    }

    @Override
    public synchronized void unsubscribe(ClientHandler client) {
        clients.remove(client);
        logger.log(Level.INFO, "new client is unsubscribing - " + client.getName());
    }
}
