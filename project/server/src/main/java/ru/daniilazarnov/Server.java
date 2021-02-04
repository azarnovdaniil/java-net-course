package ru.daniilazarnov;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Scanner;

public class Server implements Runnable {
    private ServerSocket server;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private final int PORT = 8189;
    private int acceptedClientIndex = 1;
    private Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        new Thread(new Server()).start();
    }

    @Override
    public void run() {
        try{
            server = new ServerSocket(PORT);
            System.out.println("Сервер запущен! Порт " + PORT);
                    while (true) {
                        socket = server.accept();
                        System.out.println("Клиент # " + acceptedClientIndex + " подключился!");
                        clientHandler();
                        acceptedClientIndex++;
                    }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void broadcastMsg(String msg) {
        String message = String.format("%s: %s", "Клиент # " + (acceptedClientIndex-1), msg);
        sendMsg(message);
        System.out.println(message);
    }
    public void clientHandler() {
        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            new Thread(() -> {
                try {
                    while (true) {
                        String str = in.readUTF();
                        if (str.startsWith("/")) {
                            if (str.equals("/end")) {
                                out.writeUTF("/end");
                                break;
                            }
                        } else {
                            broadcastMsg(str);
                        }
                    }
                }catch (SocketTimeoutException e){
                    sendMsg("/end");
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    System.out.println("Клиент отключился!");
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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