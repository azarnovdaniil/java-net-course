package ru.daniilazarnov;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientHandler {
    private final DataInputStream in;
    private final DataOutputStream out;

    public ClientHandler(Socket socket) {
        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        } catch (Exception e) {
            throw new RuntimeException("SWW", e);
        }
        listen();
    }

    private void listen() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(this::process);
        executorService.shutdown();
    }

    private void process() {
        receiveMessage();
    }

    public void receiveMessage() {
        while (true) {
            try {
                String message = in.readUTF();
                if (message.startsWith("-exit")) {
                    System.out.println("!- A client disconnected.");
                    break;
                } else {
                    System.out.println(":: " + message);
                }
           }
            catch (IOException e) {
                throw new RuntimeException("SWW", e);
            }
        }
    }
}

