package ru.daniilazarnov;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
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
        int number = sc.nextInt ();
        switch (number){
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
        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String getNickname() {
        return nickname;
    }

    public void sendMsg(String msg) {
        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                            }
                            if (str.equals("/regNO")) {
                                System.out.println("Регистрация не получилась\n" +
                                        "Возможно предложенные лоин или никнейм уже заняты");
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
                            }
                            break;
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

        int number = sc.nextInt ();

        switch (number){
//            case 1:
//                try {
//                    if (files.isFileCreatedAtDesktop (login)) {
//                        System.out.println ("Файл успешно создан");
//                    }
//
//                } catch (IOException e) {
//                    System.out.println ("Ошибка создания файла");
//                } break;

            case 2:
                files.downloadFileToCloud (out,login);
                System.out.println ("Файл загружен.");
                break;
            case 3:
                sc = new Scanner (System.in);
                System.out.println ("Введите название файла в формате .txt");
                String fileName = sc.nextLine ();
                String msg = String.format("/upload %s", fileName);
                try {
                    out.writeUTF (msg);
                } catch (IOException e) {
                    e.printStackTrace ();
                }
                String str = null;
                try {
                    str = in.readUTF();
                } catch (IOException e) {
                    e.printStackTrace ();
                }

                if (str != null && str.startsWith ("/uploadOK ")) {
                    System.out.println (str);
                }
                if (str != null && str.startsWith ("/uploadNO ")) {
                    System.out.println (str);
                }

                break;
//            case 4: file.isFileRenamed ();
//            case 5:
//                sc = new Scanner (System.in);
//                System.out.println ("Введите название файла в формате .txt");
//                String fileName1 = sc.nextLine ();
//                String msg1 = String.format("/delete", fileName1);
//                try {
//                    out.writeUTF (msg1);
//                } catch (IOException e) {
//                    e.printStackTrace ();
//                }
//                String str1 = null;
//                try {
//                    str1 = in.readUTF();
//                } catch (IOException e) {
//                    e.printStackTrace ();
//                }
//
//                if (str1 != null && str1.startsWith ("/deleteOK")) {
//                    System.out.println (str1);
//                }
//                if (str1 != null && str1.startsWith ("/deleteNO")) {
//                    System.out.println (str1);
//                }
            case 6:
                System.out.println ("Список файлов:");
                try {
                    out.writeUTF ("/ls");
                } catch (IOException e) {
                    e.printStackTrace ();
                    System.out.println ("Ошибка отправки команды");
                }
                String str2 = null;
                try {
                    str2 = in.readUTF();
                } catch (IOException e) {
                    e.printStackTrace ();
                }

                if (str2 != null && str2.startsWith ("/lsOK ")) {
                    System.out.println (str2);
                }
                if (str2 != null && str2.startsWith ("/lsNO")) {
                    System.out.println ("Не удалось загрузить список файлов");
                }

                break;
            case 7:
                try {
                    out.writeUTF ("/end");
                } catch (IOException e) {
                    e.printStackTrace ();
                }
                System.exit (0);
        }

    }

}


