package ru.daniilazarnov;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;


public class Server {

    private static String text;

    public static void main(String[] args) throws IOException {
        new Server();
    }

    public Server() throws IOException {
        try {
            ServerSocket serverSocket = new ServerSocket(888);

            Socket clientSocket = serverSocket.accept();

            DataInputStream in = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());

            Thread th1 = new Thread() {
                @Override
                public void run() {
                    super.run();
                    while (true) {
                        String message = null;
                        try {
                            message = in.readUTF();
                            if (isLs(message)) {
                                text = "Введите имя папки, файлы которой хотите вывести";
                                out.writeUTF(text);
                                String nameDirectory = in.readUTF();
                            }

                            if (isSend(message)) {
                                try {
                                    text = "Введите имя папки, в которой находится файл";
                                    out.writeUTF(text);
                                    String nameDirectory = in.readUTF();

                                    text = "Введите имя файла, в который нужно записать сообщение";
                                    out.writeUTF(text);
                                    String nameFile = in.readUTF();

                                    Path rootPath = Paths.get(nameDirectory);
                                    String fileToFind = File.separator + nameFile;

                                    Files.walkFileTree(rootPath, new SimpleFileVisitor<Path>() {
                                        @Override
                                        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                                            String fileString = file.toAbsolutePath().toString();
                                            out.writeUTF("pathString = " + fileString);
                                            if (fileString.endsWith(fileToFind)) {
                                                Path way = file.toAbsolutePath();
                                                out.writeUTF("file found at path: " + way);
                                                text = "Введите сообщение, которое хотите записать";
                                                out.writeUTF(text);
                                                String textToFile = in.readUTF();
                                                PrintWriter out3 = new PrintWriter(new FileWriter(String.valueOf(way)), true);
                                                out3.println(textToFile);
                                                return FileVisitResult.TERMINATE;
                                            }
                                            return FileVisitResult.CONTINUE;
                                        }
                                    });
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    }
                }

                public boolean isLs(String message) {
                    boolean l = false;
                    if (message.contains("ls")) {
                        l = true;
                    } else {
                        l = false;
                    }
                    return l;
                }

                public boolean isSend(String message) {
                    boolean s = false;
                    if (message.contains("send")) {
                        s = true;
                    } else {
                        s = false;
                    }
                    return s;
                }
            };
            th1.start();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}



