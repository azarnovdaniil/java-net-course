package ru.daniilazarnov;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client  {
//    public static void main(String[] args) {
    public static void main(String[] args) {

//        logger.info("INFO");
        Client client = new Client();

    }
public Client(){
        try {
            Socket socket = new Socket("localhost", 9999);
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            System.out.println("Клиент подключился к серверу (Порт: 8189)");

// чтение ответов сервера
            new Thread(() -> {
                try {
                    while (true) {
                        String message = in.readUTF();
                        if (message.equalsIgnoreCase("cmd auth: Status OK")) {
                            System.out.println("Session end");
                            break;
                        }
                        System.out.println(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

// написание и отправка команд серверу
            Scanner scanner = new Scanner(System.in);
            while (true) {
                try {
                    System.out.println("Enter commmand: ");
                    out.writeUTF(scanner.nextLine());
                } catch (IOException e) {
                    throw new RuntimeException("SWW", e);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
