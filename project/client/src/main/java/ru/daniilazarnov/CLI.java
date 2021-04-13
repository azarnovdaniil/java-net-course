package ru.daniilazarnov;

import ru.daniilazarnov.domain.FileMessage;
import ru.daniilazarnov.domain.MyMessage;
import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;

import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class CLI {
    private Socket socket;
    private ObjectEncoderOutputStream out;
    private ObjectDecoderInputStream in;
    public static final int MAX_OBJECT_SIZE = 100 * 1024 * 1024;
    public CLI() { }
    public void connect(String host, Integer port, String repo) throws IOException {
        String s;
        Common common = new Common();
        try {
            if (socket == null || socket.isClosed()) {
                socket = new Socket(host, port);
                out = new ObjectEncoderOutputStream(socket.getOutputStream());
                in = new ObjectDecoderInputStream(socket.getInputStream(), MAX_OBJECT_SIZE);
                new Thread(() -> {
                    while (socket.isConnected()) {
                        Object msg = null;
                        try {
                            msg = in.readObject();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (msg instanceof MyMessage) {
                            String message = ((MyMessage) msg).getText();
                            System.out.println(message);
                        } else if (msg instanceof FileMessage) {
                                common.receiveFile(msg, repo);
                        }
                    }
                }).start();

                    Scanner scanner = new Scanner(System.in);
                    while (scanner.hasNext()) {
                        s = scanner.nextLine();
                        String[] strings = s.split(" ");
                        if (s.equals("/upload") && strings.length == 2) {
                            Path senderFileAddress = Path.of(repo + "\\" + strings[1]);
                            if (Files.exists(senderFileAddress)) {
                            out.writeObject(common.sendFile(strings[1], senderFileAddress));
                            }
                                System.out.println("The specified file does not exist");
                        } else if (strings[0].equals("/delete") && s.endsWith("-l")) {
                            System.out.println((new Common()).deleteFile(strings[1], repo));
                        } else if (strings[0].equals("/rename") && s.endsWith("-l")) {
                            System.out.println((new Common()).renameFile(strings[1], strings[2], repo));
                        }  else if (strings[0].equals("/show") && s.endsWith("-l")) {
                            System.out.println((new Common()).showFiles(repo));
                        } else {
                            MyMessage textMessage = new MyMessage(s);
                            out.writeObject(textMessage);
                        }
                        out.flush();
                    }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            in.close();
            out.close();
            socket.close();
        }
    }
}

