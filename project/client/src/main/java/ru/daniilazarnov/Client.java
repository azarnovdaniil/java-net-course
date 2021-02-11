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
                                System.out.println(fm.getPartNumber() + " / " + fm.getPartsCount());

                                boolean append = true;
                                if (fm.getPartNumber() == 1) {
                                    append = false;
                                }

                                File newFile = new File("./project/clients_vault/" + login + "/" + fm.getFilename());
                                FileOutputStream fos = new FileOutputStream(newFile, append);

                                fos.write(fm.getData());
                                fos.close();

                        }
                        else if (receivedFile instanceof DirectoryInfoMessage) {
                            DirectoryInfoMessage dim = (DirectoryInfoMessage) receivedFile;

                            System.out.println(dim.getFilesAtDirectory().toString());
                        }
                        else if (receivedFile instanceof DBMessage) {
                            DBMessage dbm = (DBMessage) receivedFile;
                            System.out.println("Auth was successful");
                            login = dbm.getLogin();
                            clientHandler.setClientLogin(login);
                            Path newClientDir = Paths.get("./project/clients_vault/" + dbm.getLogin());
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
