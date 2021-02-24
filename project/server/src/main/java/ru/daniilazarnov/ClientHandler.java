package ru.daniilazarnov;

import ru.daniilazarnov.auth.*;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Logger;

import ru.daniilazarnov.commands.*;

public class ClientHandler {
    private final Server server;
    private final Socket clientSocket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private String clientUsername;
    private FilesOperations fo;
    private final static String serverPath = "project/server/files/";
    private static final Logger logger = Logger.getLogger(Server.class.getName());

    public ClientHandler(Server server, Socket clientSocket) throws SocketException {
        this.server = server;
        this.clientSocket = clientSocket;
    }
    public void handle() throws IOException {
        in = new ObjectInputStream(clientSocket.getInputStream());
        out = new ObjectOutputStream(clientSocket.getOutputStream());
        new Thread(() -> {
            try {
                logger.info("Мы в серверном потоке...");
                authentication();
                readMessage();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }).start();
    }

    private void authentication() throws IOException {
        logger.info("Ожидаем авторизацию...");
        while (true) {
            Command command = readCommand();
            if (command == null) {
                continue;
            }
            if (command.getType() == CommandType.AUTH) {
                boolean isSuccessAuth = processAuthCommand(command);
                if (isSuccessAuth) {
                    logger.info("Авторизация пройдена");
                    break;
                }
            } else {
                logger.info("Ошибка авторизации");
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
        System.out.println(clientUsername);
        if (clientUsername != null) {
            if (server.isUsernameBusy(clientUsername)) {
                logger.info("Попытка использовать логин " + clientUsername + " повторно");
                sendMessage(Command.authErrorCommand("Логин уже используется"));
                return false;
            }
            sendMessage(Command.authOkCommand(clientUsername));
            server.subscribe(this);
            return true;
        } else {
            logger.info("Логин или пароль не соответствуют действительности");
            sendMessage(Command.authErrorCommand("Логин или пароль не соответствуют действительности"));
            return false;
        }
    }

    private void readMessage() throws IOException {
        while (true) {
            Command command = readCommand();
            if (command == null) {
                continue;
            }
            switch (command.getType()) {
                case END:
                    server.unSubscribe(this);
                    return;
                case REQUEST_FILE: {
                    RequestFileData data = (RequestFileData) command.getData();
                    String username = data.getUsername();
                    String filename = data.getFilename();
                    String fullPathName = serverPath + username + "/" + filename;
                    logger.info("Клиентом " + username + " запрошено скачивание с сервера файла " + filename);
                    System.out.println("Клиентом " + username + " запрошено скачивание с сервера файла " + filename);
                    File file = new File(fullPathName);
                    if (file.exists()) {
                        fo = new FilesOperations();
                        sendMessage(Command.sendFileCommand(username, filename, fo.readFromFileToByteArr(fullPathName)));
                    } else {
                        sendMessage(Command.messageInfoCommand("Файл " + filename + " по адресу " + serverPath + username + " не найден.", "Служба размещения"));
                        logger.info("Запрошенный файл не найден. Отправлено уведомление клиенту");
                        System.out.println("Запрошенный файл не найден. Отправлено уведомление клиенту");
                    }
                    break;
                }
                case SEND_FILE: {
                    SendFileData data = (SendFileData) command.getData();
                    String username = data.getUsername();
                    String filename = data.getFilename();
                    System.out.println("Клиентом " + username + " запрошена загрузка на сервер файла " + filename);
                    byte[] arr = data.getBytes();
                    fo = new FilesOperations();
                    fo.saveToFileFromByteArr(arr,serverPath + username + "/" + filename);
                    System.out.println("Файл " + filename + " получен и скопирован в папку " + serverPath + username);
                    sendMessage(Command.messageInfoCommand("Файл " + filename + " получен и скопирован в папку " + serverPath + username, "Служба размещения"));
                    break;
                }
                case RENAME_FILE: {
                    RenameFileData data = (RenameFileData) command.getData();
                    String username = data.getUsername();
                    String filename = data.getFilename();
                    String filenameNew = data.getFilenameNew();
                    String fullPathName = serverPath + username + "/" + filename;
                    String fullPathNameNew = serverPath + username + "/" + filenameNew;
                    System.out.println("Клиентом " + username + " запрошено переименование файла на сервере " + filename);
                    File file = new File(fullPathName);
                    File fileNew = new File(fullPathNameNew);
                    if (file.exists()) {
                        if (file.renameTo(fileNew)) {
                            sendMessage(Command.messageInfoCommand("Файл " + filename + " переименован в  " + filenameNew, "Служба серверного каталога"));
                            System.out.println("Файл переименован. Отправлено уведомление клиенту");
                        } else {
                            System.out.println("Возникли непредвиденные трудности в процессе переименования");
                        }
                    } else {
                        sendMessage(Command.messageInfoCommand("Файл " + filename + " по адресу " + serverPath + username + " не найден.", "Служба серверного каталога"));
                        System.out.println("Запрошенный файл не найден. Отправлено уведомление клиенту");
                    }
                    break;
                }
                case REMOVE_FILE: {
                    RemoveFileData data = (RemoveFileData) command.getData();
                    String username = data.getUsername();
                    String filename = data.getFilename();
                    String fullPathName = serverPath + username + "/" + filename;
                    System.out.println("Клиентом " + username + " запрошено удаление с сервера файла " + fullPathName);
                    File file = new File(fullPathName);
                    if (file.exists()) {
                        if (file.delete()) {
                            sendMessage(Command.messageInfoCommand("Файл " + filename + " удален", "Служба серверного каталога"));
                            System.out.println("Файл удален. Отправлено уведомление клиенту");
                        } else {
                            sendMessage(Command.messageInfoCommand("Недостаточно прав для удаления файла", "Служба серверного каталога"));
                            System.out.println("Возникли непредвиденные трудности в процессе удаления");
                        }
                    } else {
                        sendMessage(Command.messageInfoCommand("Файл " + filename + " по адресу " + serverPath + username + " не найден.", "Служба серверного каталога"));
                        System.out.println("Запрошенный файл не найден. Отправлено уведомление клиенту");
                    }
                    break;
                }
                case GET_DIR:
                    GetDirCommandData data = (GetDirCommandData) command.getData();
                    String username = data.getUsername();
                    String dir = data.getUserDir();
                    System.out.println("Клиентом " + username + " запрошен список файлов.");
                    String dirList = "\n" + "Содержимое папки " + serverPath + username + "\n";
                    File file = new File(serverPath + username);
                    if (file.exists()) {
                        for (String item : file.list()) {
                            dirList = dirList + item + "\n";
                        }
                    }
                    sendMessage(Command.messageInfoCommand(dirList,"Служба серверного каталога"));
                    System.out.println("Список файлов отправлен.");
                    break;
                default:
                    String errorMessage = "Неизвестный тип команды: " + command.getType();
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
