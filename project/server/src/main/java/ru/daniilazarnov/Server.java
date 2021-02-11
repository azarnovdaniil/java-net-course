package ru.daniilazarnov;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    private static final Logger logger = Logger.getLogger (Server.class.getName ());
    private ServerSocket server;
    private Socket socket;
    private final int PORT = 8189;
    private List<ClientHandler> clients;
    private AuthService authService;

    public ExecutorService getExecutorService() {
        return executorService;
    }

    private ExecutorService executorService;

    public Server() {
        clients = new CopyOnWriteArrayList<>();
        executorService = Executors.newFixedThreadPool (60);

        if (!SQLHandler.connect()) {
            logger.severe ("Не удалось подключиться к БД");
            throw new RuntimeException("Не удалось подключиться к БД");
        }
        authService = new DBAuthServise();

        try {
            server = new ServerSocket(PORT);
            logger.info ("server started!");

            while (true) {
                socket = server.accept();
                logger.info ("client connected " + socket.getRemoteSocketAddress());
                new ClientHandler(this, socket);
            }

        } catch (IOException e) {
            logger.log (Level.INFO, "Клиент не подключился ", e);
            e.printStackTrace();
        } finally {
            SQLHandler.disconnect();
            logger.info ("server closed");
            executorService.shutdown ();
            try {
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void subscribe(ClientHandler clientHandler) {
        clients.add(clientHandler);
    }

    public void unsubscribe(ClientHandler clientHandler) {
        clients.remove(clientHandler);
    }

    public AuthService getAuthService() {
        return authService;
    }

    public boolean isloginAuthenticated(String login) {
        for (ClientHandler c : clients) {
            if (c.getLogin().equals(login)) {
                return true;
            }
        }
        return false;
    }

}
