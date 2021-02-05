package ru.daniilazarnov;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;


   /*
    Нереализованный протокол:
            [byte [] ] 1b управляющий байт →
            [short [][]] 2b длинна имени файла →
            [byte[]?] nb  имя файла →
            [long  [][][][][][][][]] 8b размер файла →
            [byte[]?] nb содержимое файла
     */

public class Client {
    private static Network client;
    private static BufferedReader bufferedReader = null;
    private static String msg = "◙◙◙";

    public static void sendMsg(String message) {
        client.sendMessage(message);
    }

    public static void main(String[] args) throws IOException {
        client = new Network();
        InputStream in = System.in;


        String inputLine = "";
        while (true) {
            bufferedReader = new BufferedReader(new InputStreamReader(in));
            if (client.isConnect()) {
                System.out.println("Введите сообщение:");
                inputLine = bufferedReader.readLine().trim().toLowerCase();

                switch (inputLine) {
                    case "ls":
                        System.out.println("LS");
//                        System.out.println("Files.exists: " + Files.exists(Path.of("project/client/local_storage")));
                        client.sendfile("project/client/local_storage/file_to_send.txt");
                        break;
                    case "cd":
                        System.out.println("CD");
                        break;
                    case "rm":
                        System.out.println("RM");
                        break;
                    case "/":
                        System.out.println("server command".toUpperCase());
                        sendCommandToServer();
                        break;
                    case "exit":
                        System.out.println("exit");
                        bufferedReader.close();
                        return;

                    default:
                        throw new IllegalStateException("Unexpected value: " + inputLine);
                }
            }

//            sendMsg(inputLine);
        }


    }

    private static void sendCommandToServer() {
        String inputLine = "";
        while (true) {
            if (client.isConnect()) {
                System.out.println("Команда для сервера:");
                try {
                    inputLine = bufferedReader.readLine().trim().toLowerCase();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                switch (inputLine) {
                    case "ls":
                        sendMsg(inputLine);
                        System.out.println("sendCommandToServer LS");
                        break;
                    case "cd":
                        System.out.println("sendCommandToServer CD");
                        break;
                    case "rm":
                        System.out.println("sendCommandToServer RM");
                        break;
                    case "exit":
                        System.out.println("exit");

                        return;

                    default:
                        throw new IllegalStateException("Unexpected value: " + inputLine);
                }
            }
        }


    }


}