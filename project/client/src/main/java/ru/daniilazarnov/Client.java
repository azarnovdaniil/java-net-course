package ru.daniilazarnov;

import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;

import java.io.*;
import java.net.Socket;
import java.nio.file.*;

public class Client {

    private String host;
    private int port;
    private String login;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public static void main(String[] args) {
        new Client("localhost", 8888).run();
    }

    public void run() {
        try (Socket socket = new Socket(host, port);
             ObjectEncoderOutputStream objectOut = new ObjectEncoderOutputStream(socket.getOutputStream());
             ObjectDecoderInputStream objectIn = new ObjectDecoderInputStream(socket.getInputStream(),1024 * 1024 * 100)) {

            ClientHandler clientHandler = new ClientHandler(objectOut);
            new Thread(() -> {
                try {
                    while (true) {
                        AbstractMessage receivedFile = (AbstractMessage) objectIn.readObject();

                        if (receivedFile instanceof FileMessage) {
                            FileMessage fm = (FileMessage) receivedFile;

                            Path newFile = Paths.get("./project/client/src/main/java/ru/daniilazarnov/client_vault/" + login + "/" + fm.getFilename());
                            Files.write(
                                    newFile,
                                    fm.getData(),
                                    StandardOpenOption.CREATE);
                        }
                        else if (receivedFile instanceof DirectoryInfoMessage) {
                            DirectoryInfoMessage dim = (DirectoryInfoMessage) receivedFile;

                            System.out.println(dim.getFilesAtDirectory().toString());
                        }
                        else if (receivedFile instanceof AuthMessage) {
                            AuthMessage am = (AuthMessage) receivedFile;
                            System.out.println("Auth was successful");
                            login = am.getLogin();
                            clientHandler.setClientLogin(login);
                            Path newClientDir = Paths.get("./project/client/src/main/java/ru/daniilazarnov/client_vault/" + am.getLogin());
                            if (!newClientDir.toFile().exists()) {
                                Files.createDirectories(newClientDir);
                            }
                        }
                    }
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException("SWW", e);
                }
            }).start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            while (true) {
                try {
                    String msg = reader.readLine();
                    clientHandler.chooseCommand(msg);
                } catch (IOException e) {
                    throw new RuntimeException("SWW", e);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("SWW", e);
        }
    }

}
