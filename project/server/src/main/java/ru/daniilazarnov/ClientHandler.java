package ru.daniilazarnov;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientHandler {
    private DataInputStream in;
    private DataOutputStream out;

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
        loop: while (true) {
            try {
                byte[] buffer = new byte[in.readInt()];
                int code = in.readInt();
                in.read(buffer);

                switch (code) {
                    case 1:
                        break;
                    case 2:
                        System.out.println("!- Downloading a file to be implemented.");
                        break;
                    case 3:
                        System.out.println("!- Showing files to be implemented.");
                        break;
                    case 4:
                        System.out.println("!- Removing file to be implemented.");
                        break;
                    case 5:
                        System.out.println("!- Renaming file to be implemented.");
                        break;
                    case 255:
                        System.out.println("!- A client disconnected to be implemented.");
                        break loop;
                }
           }
            catch (IOException e) {
                throw new RuntimeException("SWW", e);
            }
        }
    }
}

