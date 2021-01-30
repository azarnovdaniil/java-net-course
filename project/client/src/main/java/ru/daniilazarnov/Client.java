package ru.daniilazarnov;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Client {
    private DataInputStream in;
    private DataOutputStream out;

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 8189);
        OutputStream output = socket.getOutputStream();
        InputStream input = socket.getInputStream();

        PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(output, "UTF-8"));


        Scanner scanner = new Scanner(input);
        String str;

        while (!socket.isOutputShutdown()) {

            if (scanner.hasNext()) {
                str = scanner.nextLine();// считывание строк из стандартного ввода

                printWriter.println(str);// и отправка на сервер
                printWriter.flush();

                String msg;
                while (!socket.isInputShutdown()) { // ждем сообщений

                    msg = scanner.nextLine();// считывание сообщений с сервера
                    System.out.println(msg); //выводим в консоль его
                    if (msg.equals("/gg"))
                        break;
                }
                if (str.equalsIgnoreCase("/bye"))
                    break;

            }
        }
    }
}