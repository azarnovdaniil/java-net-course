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
    Scanner sc;


    public void startApp() {
        sc = new Scanner(System.in);
        System.out.println("Введите номер операции: 1 - войти, 2 - зарегистрироваться");
        int number = sc.nextInt ();
        switch (number){
            case 1: tryToAuth ();
            case 2: tryToReg ();
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
                                    System.out.println ("Папка с вашим логином создана в директории desktop");
                                } else {
                                    System.out.println ("Создайте папку (имя папки - ваш логин) " +
                                            "в директории client\\desktop.");
                                }
                            }
                            if (str.equals("/regNO")) {
                                System.out.println("Регистрация не получилась\n" +
                                        "Возможно предложенные лоин или никнейм уже заняты");
                            }

                            if (str.startsWith("/authOK")) {
                                nickname = str.split("\\s")[1];
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
                        String str = in.readUTF();
                        

                    }

                } catch (IOException e){

                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readCommFromConsole() {

        System.out.println ("Введите номер операции: ");
        System.out.println ( "1 -  Создать файл на Рабочем столе. " +
                "2 - Загрузить файл в Облако." +
                "3 - Скачать файл из Облака." +
                "4 - Переименовать файл в Облаке." +
                "5 - Удалить файл в Облаке. " +
                "6 - Посмотреть список файлов в Облаке");

        int number = sc.nextInt ();

        switch (number){
            case 1:
                try {
                    files.isFileCreatedAtDesktop (this.login);
                } catch (IOException e) {
                    e.printStackTrace ();
                }
//            case 2: file.downloadFileToCloud ();
//            case 3: uploadFileToCloud ();
//            case 4: file.isFileRenamed ();
//            case 5: file.isFileDelited ();
//            case 6: file.ls (this.login);
        }
    }
}


