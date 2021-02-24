package ru.daniilazarnov;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import ru.daniilazarnov.commands.*;
import java.util.Scanner;
import java.util.logging.Logger;
import java.io.*;

public class Network {

    private static final String SERVER_ADRESS = "localhost";
    private static final int SERVER_PORT = 8190;
    private final static int MIN_FILENAME_LENGTH = 2;
    private static final Logger logger = Logger.getLogger(Network.class.getName());
    private final String host;
    private final int port;
    private ObjectOutputStream dataOutputStream;
    private ObjectInputStream dataInputStream;
    private Socket socket;
    private String username;
    private FilesOperations fo;
    private final static String clientPath = "project/client/files/";
    private final static String HELP = "ls - показать список файлов на сервере\nul - получить файл с сервера\ndl - загрузить файл на сервер\nrm - удалить файл с сервера\nrn - переименовать файл на сервере\nq - выйти";

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

    public void waitMessage() {
        Thread thread = new Thread( () -> {
            Scanner scanner = new Scanner(System.in);
            String login = "";
            String password = "";

            // Блок авторизации
            while (true) {
                System.out.print("Введите логин: ");
                if (scanner.hasNext()) {
                    login = scanner.nextLine();
                } else {
                    continue;
                }
                System.out.print("Введите пароль: ");
                if (scanner.hasNext()) {
                    password = scanner.nextLine();
                } else {
                    continue;
                }

                String authErrorMessage = sendAuthCommand(login, password);
                if (authErrorMessage == null) {
                    break;
                }
                else {
                    System.out.println("Ошибка авторизации");
                    continue;
                }
            }

            // Основной блок
            try {
                while (true) {
                    System.out.print("Введите команду: ");
                    String inputScanner = "";
                    String inputCommand = "";
                    String requestfile = "";

                    if (scanner.hasNext()) {
                        inputScanner = scanner.nextLine();
                    }
                    else {
                        continue;
                    }
                    String[] inputToken = inputScanner.split(" ");
                    inputCommand = inputToken[0];
                    switch (inputCommand) {
                        case "ul":  // получение файла с сервера
                            if (inputToken.length > 1) {
                                requestfile = inputToken[1];
                                if (requestfile.length() > MIN_FILENAME_LENGTH) {
                                    try {
                                        sendMessage(Command.requestFileCommand(username, requestfile));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    System.out.println("Укажите имя файла в формате *.*");
                                    continue;
                                }
                            } else {
                                System.out.println("Укажите имя файла через пробел после команды.");
                                continue;
                            }
                            break;
                        case "dl":  // отправка файла на сервер
                            if (inputToken.length > 1) {
                                requestfile = inputToken[1];
                                if (requestfile.length() > MIN_FILENAME_LENGTH) {
                                    String fullPathName = clientPath + username + "/" + requestfile;
                                    File file = new File(fullPathName);
                                    if (file.exists()) {
                                        try {
                                            fo = new FilesOperations();
                                            sendMessage(Command.sendFileCommand(username, requestfile, fo.readFromFileToByteArr(fullPathName)));
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        System.out.println("Указанный файл по адресу" +clientPath + username + " не найден.");
                                        continue;
                                    }
                                } else {
                                    System.out.println("Укажите имя файла в формате *.*");
                                    continue;
                                }
                            } else {
                                System.out.println("Укажите имя файла через пробел после команды.");
                                continue;
                            }
                            break;
                        case "q":   //завершение пользовательского сеанса
                            try {
                                    this.sendMessage(Command.endCommand());
                            } catch (IOException e) {
                                    e.printStackTrace();
                            }
                            return;
                        case "ls":  // получение списка файлов хранимых на сервере (зависит от пользователя)
                            try {
                                this.sendMessage(Command.getDirCommand(username));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        case "rm":  // удаление файла, хранимого на сервере (зависит от пользователя)
                            if (inputToken.length > 1) {
                                requestfile = inputToken[1];
                                try {
                                    this.sendMessage(Command.removeFileCommand(username, requestfile));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                System.out.println("Укажите имя файла через пробел после команды.");
                                continue;
                            }
                            break;
                        case "rn":  // переименование файла, хранимого на сервере (зависит от пользователя)
                            if (inputToken.length > 2) {
                                String oldNameFile = inputToken[1];
                                String newNameFile = inputToken[2];
                                try {
                                    this.sendMessage(Command.renameFileCommand(username, oldNameFile, newNameFile));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                System.out.println("Укажите старое и новое имя файла через пробел после команды.");
                                continue;
                            }
                            break;
                        case "help":  // получение подсказки по командам
                            System.out.println(HELP);
                            continue;
                        default:
                            System.out.println("Неизвестная команда");
                            continue;
                    }

                    Command command = readCommand();
                    switch (command.getType()) {
                    case INFO_MESSAGE: {
                        MessageInfoCommandData data = (MessageInfoCommandData) command.getData();
                        String message = data.getMessage();
                        String sender = data.getSender();
                        System.out.println("Получено сообщение от " + sender + " :" + message);
                        //String formattedMessage = sender != null ? String.format("%s: %s", sender, message) : message;
                        //logger.info(formattedMessage);
                        break;
                    }
                    case SEND_FILE: {
                        SendFileData data = (SendFileData) command.getData();
                        String username = data.getUsername();
                        String filename = data.getFilename();
                        byte[] arr = data.getBytes();
                        fo = new FilesOperations();
                        fo.saveToFileFromByteArr(arr,clientPath + username + "/" + filename);
                        System.out.println("Файл " + filename + " получен и скопирован в папку " + clientPath + username);
                        break;
                    }
                    case ERROR: {
                        ErrorCommandData data = (ErrorCommandData) command.getData();
                        String errorMessage = data.getErrorMessage();
                        System.out.println("Получено сообщение от сервера: " + errorMessage);
                        //logger.info("ОШибка в блоке switch: " + errorMessage);
                        break;
                    }
                    default:
                        System.out.println("Получена неизвестная команда от сервера: " + command.getType());
                        //logger.info("Unknown command from server!" + command.getType().toString());
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
                    return "Необрабатываемая команда!";
                case ERROR: {
                    AuthErrorCommandData data = (AuthErrorCommandData) command.getData();
                    return data.getErrorMessage();
                }
                default:
                    return "Неизвестная команда: " + command.getType();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public void checkAuth(String login, String password) {
        if(login.isEmpty() || password.isEmpty()) {
            System.out.println("Поля не должны быть пустыми. Ошибка ввода");
            return;
        }
    }

    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
