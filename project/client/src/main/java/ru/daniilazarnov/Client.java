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

    private Scanner scanner = new Scanner(System.in);


    public static void main(String[] args) {
        new Client().connect();
    }

    private void connect() {
        try {
            socket = new Socket(IP_ADDRESS, PORT);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            new Thread(() -> {
                try {
                    while (true) {
                        String str = scanner.nextLine();
                        if (str.startsWith("/"))
                        {
                            if (str.equals("/end")) {
                                break;
                            }
                        }
                        else {
                            sendMsg(str);
                        }
                    }
                } catch (RuntimeException e) {
                    System.out.println(e.getMessage());
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(String msg) {
        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
