package ru.daniilazarnov;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.*;

public class Controller {
    private static String name;
    public static String getName() {
        return name;
    }

    static void doAuthorization() {
        Commands.printMessage("Введите логин");
        String login = Commands.getText();
        Commands.printMessage("Введите пароль");
        String password = Commands.getText();
        short loginLength = (short) login.length();
        short passwordLength = (short) password.length();
        try {
            Socket socket = new Socket("localhost", 8189);
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            out.writeByte(Commands.getCommandLogin());
            out.writeShort(loginLength);
            out.write(login.getBytes());
            out.writeShort(passwordLength);
            out.write(password.getBytes());
            DataInputStream in = new DataInputStream(socket.getInputStream());
            byte[] bytes = in.readAllBytes();
            if (bytes[0] == Commands.getNameNotFound()) {
                Commands.printMessage("Не верный логин или пароль");
            } else {
                name = new String(bytes);
                Commands.printMessage("Авторизация выполнена. Добрый день " + name);
            }
            in.close();
            out.close();
            socket.close();
        } catch (Exception e) {
            Commands.printMessage("Введены некорректные данные");
        }
    }

    static void upload() {
        String filePath = "";
        Commands.printMessage("1.Загрузить с локальной папке\n2.Загрузить по указанному пути\n0.Назад");
        int choice = Commands.choiceBetweenThree();
        if (choice == 0) {
            return;
        }
        if (choice == 1) {
            Commands.printMessage("Введите имя файла");
            String fileName = Commands.getText();
            filePath = "client_files/" + fileName;
        }
        if (choice == 2) {
            Commands.printMessage("Введите путь к файлу");
            filePath = Commands.getText();
        }
        if (Files.exists(Paths.get(filePath))) {
            try {
                File file = new File(filePath);
                String fileName = file.getName();
                short nameLength = (short) name.length();
                short fileNameLength = (short) fileName.length();
                Socket socket = new Socket("localhost", 8189);
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                out.writeByte(Commands.getCommandUpload());
                out.writeShort(nameLength);
                out.write(name.getBytes());
                out.writeShort(fileNameLength);
                out.write(fileName.getBytes());
                out.writeLong(file.length());
                byte[] buff = Files.readAllBytes(Paths.get(filePath));
                out.write(buff);
                DataInputStream in = new DataInputStream(socket.getInputStream());
                byte[] bytes = in.readAllBytes();
                if (bytes.length == 1 && bytes[0] == Commands.getLogOut()) {
                    Commands.printMessage("Ошибка аутентификации");
                } else {
                    Commands.printMessage("> " + new String(bytes));
                }
                in.close();
                out.close();
                socket.close();
            } catch (IOException e) {
                Commands.printMessage("Ошибка чтения файла");
            }
        } else {
            Commands.printMessage("Неверно указан путь к файлу");
        }
    }

    static void download() {
        String filePath = "client_files/";
        Commands.printMessage("1.Сохранить в локальной папке\n2.Сохранить по указанному пути\n0.Назад");
        int choice = Commands.choiceBetweenThree();
        if (choice == 0) {
            return;
        }
        Commands.printMessage("Введите имя файла");
        String fileName = Commands.getText();
        if (choice == 2) {
            Commands.printMessage("Введите путь сохранения");
            filePath = Commands.getText();
        }
        try {
            short nameLength = (short) name.length();
            short fileNameLength = (short) fileName.length();
            Socket socket = new Socket("localhost", 8189);
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            out.writeByte(Commands.getCommandDownload());
            out.writeShort(nameLength);
            out.write(name.getBytes());
            out.writeShort(fileNameLength);
            out.write(fileName.getBytes());
            DataInputStream in = new DataInputStream(socket.getInputStream());
            byte[] bytes = in.readAllBytes();
            if (bytes.length == 1 && bytes[0] == Commands.getLogOut()) {
                Commands.printMessage("Ошибка аутентификации");
            } else {
                if (bytes[0] == Commands.getCommandDownload()) {
                    byte[] finalBytes = new byte[bytes.length - 1];
                    System.arraycopy(bytes, 1, finalBytes, 0, bytes.length - 1);
                    Path path = Paths.get(filePath + fileName);
                    Files.write(path, finalBytes);
                    Commands.printMessage("> Сохранение " + filePath + fileName + " выполнено");
                } else {
                    Commands.printMessage("> " + new String(bytes));
                }
            }
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void delete() {
        Commands.printMessage("1.Удалить в папке сервера\n2.Удалить в локальной папке\n0.Назад");
        int choice = Commands.choiceBetweenThree();
        if (choice == 0) {
            return;
        }
        Commands.printMessage("Введите имя файла");
        String fileName = Commands.getText();
        if (choice == 1) {
            try {
                short nameLength = (short) name.length();
                short fileNameLength = (short) fileName.length();
                Socket socket = new Socket("localhost", 8189);
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                out.writeByte(Commands.getCommandDelete());
                out.writeShort(nameLength);
                out.write(name.getBytes());
                out.writeShort(fileNameLength);
                out.write(fileName.getBytes());
                DataInputStream in = new DataInputStream(socket.getInputStream());
                byte[] bytes = in.readAllBytes();
                if (bytes.length == 1 && bytes[0] == Commands.getLogOut()) {
                    Commands.printMessage("Ошибка аутентификации");
                } else {
                    Commands.printMessage("> " + new String(bytes));
                }
                in.close();
                out.close();
                socket.close();
            } catch (IOException e) {
                Commands.printMessage("Ошибка соединения");
            }
        }
        if (choice == 2) {
            if (Files.exists(Paths.get("client_files/" + fileName))) {
                try {
                    Files.delete(Paths.get("client_files/" + fileName));
                } catch (IOException e) {
                    Commands.printMessage("Ошибка доступа к папке");
                }
                Commands.printMessage("> Успешно удалено");
            } else {
                Commands.printMessage("> Данный файл отсутствует");
            }
        }
    }

    static void view() {
        Commands.printMessage("1.Просмотр файлов в папке сервера\n2.Просмотр файлов в локальнои папке\n0.Назад");
        int choice = Commands.choiceBetweenThree();
        if (choice == 0) {
            return;
        }
        if (choice == 1) {
            try {
                short nameLength = (short) name.length();
                Socket socket = new Socket("localhost", 8189);
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                out.writeByte(Commands.getCommandView());
                out.writeShort(nameLength);
                out.write(name.getBytes());
                DataInputStream in = new DataInputStream(socket.getInputStream());
                byte[] bytes = in.readAllBytes();
                if (bytes.length == 1 && bytes[0] == Commands.getLogOut()) {
                    Commands.printMessage("Ошибка аутентификации");
                } else {
                    String res = new String(bytes);
                    res = res.substring(0, (res.length() - 1));
                    res = res.replace(";", "\n> ");
                    Commands.printMessage("> " + res);
                }
                in.close();
                out.close();
                socket.close();
            } catch (Exception e) {
                Commands.printMessage("Ошибка соединения");
            }
        }
        if (choice == 2) {
            StringBuilder sb = new StringBuilder();
            Path dir = Paths.get("client_files/");
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
                for (Path file : stream) {
                    sb.append(file.getFileName()).append(";");
                }
                if (sb.length() > 0) {
                    String res = sb.toString();
                    if (!res.equals("Пусто")) {
                        res = res.substring(0, (res.length() - 1));
                    }
                    res = res.replace(";", "\n> ");
                    Commands.printMessage("> " + res);
                } else {
                    Commands.printMessage("> Пусто");
                }
            } catch (IOException | DirectoryIteratorException e) {
                Commands.printMessage("Ошибка чтения");
            }
        }
    }
}
