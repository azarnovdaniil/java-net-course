package ru.daniilazarnov;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import ru.daniilazarnov.commands.*;
//import javafx.application.Platform;
import java.util.logging.Logger;

import java.io.*;

public class Network {

    private static final String SERVER_ADRESS = "localhost";
    private static final int SERVER_PORT = 8190;
    private static final Logger logger = Logger.getLogger(Network.class.getName());
    private final String host;
    private final int port;
    private ObjectOutputStream dataOutputStream;
    private ObjectInputStream dataInputStream;
    private Socket socket;
    private String username;
    private FilesOperations fo;
    private final static String serverfile = "project/server/files/server.txt";

    public ObjectOutputStream getDataOutputStream() {
        return dataOutputStream;
    }

    public ObjectInputStream getDataInputStream() {
        return dataInputStream;
    }

    public String getUsername() {
        return username;
    }

    public Socket getSocket() {
        return socket;
    }

    public Network() {
        this(SERVER_ADRESS, SERVER_PORT);
    }

    public Network(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public boolean connect() {
        try {
            socket = new Socket(host, port);
            dataOutputStream = new ObjectOutputStream(socket.getOutputStream());
            dataInputStream = new ObjectInputStream(socket.getInputStream());
            return true;
        } catch (IOException e) {
            System.out.println("Соединение не было установлено!");
            e.printStackTrace();
            return false;
        }
    }

    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void waitMessage() {
        Thread thread = new Thread( () -> {
            System.out.println("Сделан запрос файла " + serverfile);
            try {
                this.sendMessage(Command.requestFileCommand("username", serverfile));
            } catch (IOException e) {
                e.printStackTrace();
            }
            try { while (true) {
                Command command = readCommand();
                if(command == null) {
                    logger.info("Ошибка сервера. Получена неверная команда");
                    continue;
                }
                switch (command.getType()) {
                    case INFO_MESSAGE: {
                        MessageInfoCommandData data = (MessageInfoCommandData) command.getData();
                        String message = data.getMessage();
                        String sender = data.getSender();
                        String formattedMessage = sender != null ? String.format("%s: %s", sender, message) : message;
//                        Platform.runLater(() -> {
//                            chatController.appendMessage(formattedMessage);
//                        });
                        logger.info(formattedMessage);
                        break;
                    }
                    case SEND_FILE: {
                        SendFileData data = (SendFileData) command.getData();
                        String username = data.getUsername();
                        String filename = data.getFilename();
                        byte[] arr = data.getBytes();
                        String formattedMessage = username != null ? String.format("%s %s", username, filename) : filename;
                        logger.info(formattedMessage);
                        System.out.println("Получен массив длиной " + arr.length);
                        fo = new FilesOperations();
                        fo.saveToFileFromByteArr(arr,filename);
//                        try (OutputStream out = new BufferedOutputStream(new FileOutputStream(filename))) {
//                            out.write(arr);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
                        break;
                    }
                    case ERROR: {
                        ErrorCommandData data = (ErrorCommandData) command.getData();
                        String errorMessage = data.getErrorMessage();
//                        Platform.runLater(() -> {
//                            chatController.showError("Server error", errorMessage);
//                        });
                        logger.info("ОШибка в блоке switch: " + errorMessage);
                        break;
                    }
                    default:
//                        Platform.runLater(() -> {
//                            chatController.showError("Unknown command from server!", command.getType().toString());
//                        });
                        logger.info("Unknown command from server!" + command.getType().toString());
                }
            }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Соединение потеряно!");
            }
        });
        //thread.setDaemon(true);
        thread.start();
    }

    public String sendAuthCommand(String login, String password) {
        try {
            Command authCommand = Command.authCommand(login, password);
            dataOutputStream.writeObject(authCommand);
            Command command = readCommand();
            if (command == null) {
                return "Ошибка чтения команды с сервера";
            }
            switch (command.getType()) {
                case AUTH_OK: {
                    AuthOkCommandData data = (AuthOkCommandData) command.getData();
                    this.username = data.getUsername();
                    return null;
                }
                case AUTH_ERROR:
                case ERROR: {
                    AuthErrorCommandData data = (AuthErrorCommandData) command.getData();
                    return data.getErrorMessage();
                }
                default:
                    return "Unknown type of command: " + command.getType();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public void sendMessage(Command command) throws IOException {
        dataOutputStream.writeObject(command);
    }

    private Command readCommand() throws IOException {
        try {
            return (Command) dataInputStream.readObject();

        } catch (ClassNotFoundException e) {
            String errorMessage = "Получен неизвестный объект";
            System.err.println(errorMessage);
            e.printStackTrace();
            sendMessage(Command.errorCommand(errorMessage));
            return null;
        }
    }

    public void checkAuth(String login, String password) {

        if(login.isEmpty() || password.isEmpty()) {
            System.out.println("Поля не должны быть пустыми. Ошибка ввода");
            return;
        }

        String authErrorMessage = sendAuthCommand(login, password);
        if (authErrorMessage == null) {
            waitMessage();
        }
        else {
            System.out.println("Ошибка авторизации");
        }

    }
}
