package server.chat;

import clientserver.Command;
import server.chat.auth.BaseAuthService;
import server.chat.handler.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyServer {
    private final ServerSocket serverSocket;
    private final BaseAuthService authService;
    private final List<ClientHandler> clientHandlers = new ArrayList<>();
    private final ExecutorService executor;
    private static final Logger logger = Logger.getLogger(MyServer.class.getName());


    public MyServer(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.authService = new BaseAuthService();
        this.executor = Executors.newFixedThreadPool(100);
    }

    public void start() throws IOException {
        System.out.println("Сервер запущен");
        logger.log(Level.INFO, "Сервер запущен");
        //consoleRead();

        try {
            while (true) {
                waitAndProcessNewClientConnection();
            }
        } catch (IOException e) {
            logger.log(Level.WARNING, "Не удалось создать новое подключение");
            e.printStackTrace();
        } finally {
            executor.shutdownNow();
            authService.close();
            serverSocket.close();
        }
    }

    private void consoleRead() {
        Thread threadSout = new Thread(() -> {
            while (true) {
                Scanner scanner = new Scanner(System.in);
                if (scanner.hasNext()) {
                    String message = scanner.nextLine();
                    if (message.equals("/exit")) {
                        break;
                    }
                    broadcastMessage(Command.messageInfoCommand(message, "Server"), null);
                }
            }
        });
        threadSout.setDaemon(true);
        threadSout.start();
    }

    private void waitAndProcessNewClientConnection() throws IOException {
        Socket clientSocket = serverSocket.accept();
        logger.log(Level.INFO, "Соединение установлено. Ожидаем авторизации...");
        processClientConnection(clientSocket);
    }

    private void processClientConnection(Socket clientSocket) throws IOException {
        ClientHandler clientHandler = new ClientHandler(this, clientSocket);
        executor.execute(clientHandler.handle());
    }

    public BaseAuthService getAuthService() {
        return authService;
    }

    public synchronized boolean isUserBusy(String userName) {
        for (ClientHandler clientHandler : clientHandlers) {
            if (clientHandler.getUserName().equals(userName)) {
                return true;
            }
        }
        return false;
    }

    public synchronized void addUser(ClientHandler clientHandler) {
        clientHandlers.add(clientHandler);
        List<String> usernames = getAllUsernames();
        broadcastMessage(Command.updateUsersListCommand(usernames),null);
        System.out.println("Пользователь " + clientHandler.getUserName() + " успешно подключен");
    }

    private List<String> getAllUsernames() {
        List<String> usernames = new ArrayList<>();
        for (ClientHandler clientHandler : clientHandlers) {
            usernames.add(clientHandler.getUserName());
        }
        return usernames;
    }

    public synchronized void deleteUser(ClientHandler clientHandler) {
        clientHandlers.remove(clientHandler);
        List<String> usernames = getAllUsernames();
        broadcastMessage(Command.updateUsersListCommand(usernames),null);
        System.out.println("Пользователь " + clientHandler.getUserName() + " вышел из чата");
    }

    public synchronized void broadcastMessage(Command command, ClientHandler sender) {
        for (ClientHandler clientHandler : clientHandlers) {
            if (!clientHandler.equals(sender)) {
                try {
                    clientHandler.sendMessage(command);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public synchronized void personalMessage(Command command, String userName) {
        for (ClientHandler clientHandler : clientHandlers) {
            if (clientHandler.getUserName().equals(userName)) {
                try {
                    clientHandler.sendMessage(command);
                    return;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
