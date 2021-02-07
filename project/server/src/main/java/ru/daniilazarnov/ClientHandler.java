package ru.daniilazarnov;

import ru.daniilazarnov.auth.*;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;

import ru.daniilazarnov.commands.*;

public class ClientHandler {
    private final Server server;
    private final Socket clientSocket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private String clientUsername;
    private FilesOperations fo;
    private final static String clientfile = "project/client/files/server.txt";

    public ClientHandler(Server server, Socket clientSocket) throws SocketException {
        this.server = server;
        this.clientSocket = clientSocket;
    }
    public void handle() throws IOException {
        in = new ObjectInputStream(clientSocket.getInputStream());
        out = new ObjectOutputStream(clientSocket.getOutputStream());
        new Thread(() -> {
            try {
                System.out.println("Мы в серверном потоке...");
                //authentication();
                readMessage();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }).start();
    }

    private void authentication() throws IOException {
        System.out.println("Ожидаем авторизацию...");
        while (true) {
            Command command = readCommand();
            if (command == null) {
                continue;
            }
            if (command.getType() == CommandType.AUTH) {
                boolean isSuccessAuth = processAuthCommand(command);
                if (isSuccessAuth) {
                    break;
                }
            } else {
                sendMessage(Command.authErrorCommand("Ошибка авторизации"));
            }
        }
    }

    private boolean processAuthCommand(Command command) throws IOException {
        AuthCommandData cmdData = (AuthCommandData) command.getData();
        String login = cmdData.getLogin();
        String password = cmdData.getPassword();
        AuthService authService = server.getAuthService();
        this.clientUsername = authService.getUsernameByLoginAndPassword(login, password);
        if (clientUsername != null) {
            if (server.isUsernameBusy(clientUsername)) {
                sendMessage(Command.authErrorCommand("Логин уже используется"));
                return false;
            }
            sendMessage(Command.authOkCommand(clientUsername));
            String message = String.format(">>> %s присоединился к чату", clientUsername);
            return true;
        } else {
            sendMessage(Command.authErrorCommand("Логин или пароль не соответствуют действительности"));
            return false;
        }
    }

    private void readMessage() throws IOException {
        while (true) {
            System.out.println("Читаем сообщение от клиента...");
            Command command = readCommand();
            if (command == null) {
                continue;
            }
            System.out.println("Получено сообщение " + command.getType());
            switch (command.getType()) {
                case END:
                    return;
                case PUBLIC_MESSAGE: {
                    PublicMessageCommandData data = (PublicMessageCommandData) command.getData();
                    String message = data.getMessage();
                    String sender = data.getSender();
                    server.broadcastMessage(this, Command.messageInfoCommand(message, sender));
                    break;
                }
                case REQUEST_FILE: {
                    System.out.println("Обработка сообщения REQUEST_FILE");
                    RequestFileData data = (RequestFileData) command.getData();
                    String username = data.getUsername();
                    String filename = data.getFilename();
                    System.out.println("Запрошен файл: " + filename);
                    fo = new FilesOperations();
                    sendMessage(Command.sendFileCommand(username, clientfile, fo.readFromFileToByteArr(filename)));
                    break;
                }
                case SEND_FILE: {
                    SendFileData data = (SendFileData) command.getData();
                    String username = data.getUsername();
                    String filename = data.getFilename();
                    byte[] arr = data.getBytes();

                    break;
                }
                default:
                    String errorMessage = "Неизвестный тип команды" + command.getType();
                    System.err.println(errorMessage);
                    sendMessage(Command.errorCommand(errorMessage));
            }
        }
    }

    private Command readCommand() throws IOException {
        try {
            return (Command) in.readObject();
        } catch (ClassNotFoundException e) {
            String errorMessage = "Получен неизвестный объект";
            System.err.println(errorMessage);
            e.printStackTrace();
            return null;
        }
    }

    public String getClientUsername() {
        return clientUsername;
    }

    public void sendMessage(Command command) throws IOException {
        out.writeObject(command);
    }

}
