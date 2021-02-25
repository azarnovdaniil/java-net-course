package ru.daniilazarnov;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private final String IP_ADDRESS = "localhost";
    private final int PORT = 8189;

    private DataInputStream in;
    private DataOutputStream out;


    private String nickname;
    private String login;
    private String password;
    private MyDesktopFiles files;
    public Scanner sc;


    public void startApp() {
        sc = new Scanner(System.in);
        System.out.println("Введите номер операции: 1 - войти, 2 - зарегистрироваться");
        switch (getNumberFromConsole (3)){
            case 1: tryToAuth ();
                break;
            case 2: tryToReg ();
                break;
        }
    }

    public void tryToAuth() {
        if (socket == null || socket.isClosed()) {
            connect();
        }
        sc = new Scanner(System.in);

        System.out.println ("Введите логин:");
        login = sc.nextLine();

        System.out.println ("Введите пароль:");
        password = sc.nextLine();

        String msg = String.format("/auth %s %s", login, password);
        sendCommand (msg);
    }

    public void tryToReg() {
        sc = new Scanner(System.in);

        System.out.println ("Введите ваше имя:");
        nickname = sc.nextLine ();

        System.out.println ("Придумайте логин:");
        login = sc.nextLine();

        System.out.println ("Придумайте пароль:");
        password = sc.nextLine();

        String msg = String.format("/reg %s %s %s", login, password, nickname);

        if (socket == null || socket.isClosed()) {
            connect();
        }
        sendCommand (msg);
    }

    private void connect() {
        try {
            socket = new Socket(IP_ADDRESS, PORT);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            files = new MyDesktopFiles ();

            new Thread(() -> {
                try {
                    while (true) {
                        String str = in.readUTF();
                            if (str.equals("/regOK")) {
                                System.out.println ("Регистрация прошла успешно.");

                                if (files.isNewClientDirCreated (this.login)){
                                    System.out.println ("Папка с вашим логином создана в директории desktop.");
                                } else {
                                    System.out.println ("Создайте папку (имя папки - ваш логин) " +
                                            "в директории client\\desktop.");
                                }
                                break;
                            }
                            if (str.equals("/regNO")) {
                                System.out.println("Регистрация не получилась\n" +
                                        "Возможно предложенные лоин или никнейм уже заняты");
                                System.exit (0);
                            }

                            if (str.startsWith("/authOK ")) {
                                String[] token = str.split("\\s", 2);
                                nickname = token[1];
                                System.out.println ("Здравствуйте, " + this.nickname);
                                break;
                            }


                            if (str.startsWith("/authNO")) {
                                System.out.println ("Авторизация не получилась\n" +
                                        "Возможно неверный логин или пароль");
                                System.exit (0);
                            }
//
                        }
                    while (true) {
                        readCommFromConsole ();
                    }

                } catch (IOException e){

                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readCommFromConsole() {

        sc = new Scanner(System.in);
        System.out.println ("Введите номер операции: ");
        System.out.println ( "1 -  Создать файл на Рабочем столе. " +
                "2 - Загрузить файл в Облако." +
                "3 - Скачать файл из Облака." +
                "4 - Переименовать файл в Облаке." +
                "5 - Удалить файл в Облаке. " +
                "6 - Посмотреть список файлов в Облаке. " +
                "7 - Выйти из приложения.");

        switch (getNumberFromConsole (8)){
            case 1:
                try {
                    if (files.isFileCreatedAtDesktop (login)){
                        System.out.println ("Файл успешно создан.");
                    } else System.out.println ("Ошибка создания файла.");
                } catch (IOException e) {
                    e.printStackTrace ();
                }
                break;

            case 2:
                files.downloadFileToCloud (out,login);
                break;

            case 3:
                sc = new Scanner (System.in);
                System.out.println ("Введите название файла в формате .txt");
                String fileName = sc.nextLine ();
                String msg = String.format("/upload %s", fileName);
                sendCommand (msg);
                String str = readServerInfo ();
                if (str != null) {
                    System.out.println (str);
                }
                break;
            case 4:
                sc = new Scanner (System.in);
                System.out.println ("Введите текущее название файла в формате .txt");
                String fileOldName = sc.nextLine ();
                System.out.println ("Введите НОВОЕ ИМЯ файла в формате .txt");
                String fileNewName = sc.nextLine ();
                String msg1 = String.format("/rename %s %s", fileOldName, fileNewName);
                sendCommand (msg1);
                String str1 = readServerInfo ();
                if (str1 != null) {
                    System.out.println (str1);
                }
                break;

            case 5:
                sc = new Scanner (System.in);
                System.out.println ("Введите название файла в формате .txt");
                String fileName2 = sc.nextLine ();
                String msg2 = String.format("/delete %s", fileName2);
                sendCommand (msg2);
                String str2 = readServerInfo ();
                if (str2 != null) {
                    System.out.println (str2);
                }
                break;

            case 6:
                System.out.println ("Список файлов:");
                sendCommand ("/ls");
                String str3 = readServerInfo ();
                if (str3 != null) {
                    System.out.println (str3);
                }
                break;

            case 7:
                sendCommand ("/end");
                System.exit (0);
        }

    }

    public void sendCommand(String msg) {
        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readServerInfo() {
        String str = null;
        try {
            str = in.readUTF();
        } catch (IOException e) {
            e.printStackTrace ();
        }
        return str;
    }

    Integer getNumberFromConsole (int radix) {
        while (!sc.hasNextInt (radix)) {
            System.out.println("Введите корректный номер операции!");
            sc.nextLine();
        }
        return sc.nextInt ();
    }

}


