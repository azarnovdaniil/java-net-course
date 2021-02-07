package ru.daniilazarnov;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.List;


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

            EventLoopGroup auth = new NioEventLoopGroup(1);
            EventLoopGroup worker = new NioEventLoopGroup();

            Thread th1 = new Thread() {
                @Override
                public void run() {
                    super.run();
                    while (true) {
                        String message = null;
                        try {
                            message = in.readUTF();
                            if (message.contains("ls")) {
                                text = "Введите имя папки, файлы которой хотите вывести";
                                out.writeUTF(text);
                                String nameDirectory = in.readUTF();
                                File dir = new File(nameDirectory);
                                File[] arrFiles = dir.listFiles();
                                List<File> lst = Arrays.asList(arrFiles);
                                out.writeUTF(String.valueOf(lst));
                            }

                            if (message.contains("sendFile")){
                                text = "Введите имя файла, который хотите отправить";
                                out.writeUTF(text);
                                String nameFileToSend = in.readUTF();




                            }

                            if (message.contains("delete")) {
                                text = "Введите имя папки с файлом, который хотите удалить";
                                out.writeUTF(text);
                                String nameDirectoryWithFileDelete = in.readUTF();
                                text = "Введите имя файла, который хотите удалить";
                                out.writeUTF(text);
                                String nameFileDelete = in.readUTF();
                                Path rootPath = Paths.get(nameDirectoryWithFileDelete + "/" + nameFileDelete);
                                Files.walkFileTree(rootPath, new SimpleFileVisitor<Path>(){
                                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                                        System.out.println("delete file: " + file.toString());
                                        Files.delete(file);
                                        return FileVisitResult.CONTINUE;
                                    }
                                });
                            }

                            if (message.contains("create")){
                                text = "Введите имя папки, в которой хотите создать файл";
                                out.writeUTF(text);
                                String nameDirectoryWithFileCreate = in.readUTF();
                                text = "Введите имя файла";
                                out.writeUTF(text);
                                String nameFileCreate = in.readUTF();
                                Path path = Paths.get(nameDirectoryWithFileCreate + "/" + nameFileCreate);
                                Path newFile = Files.createFile(path);
                            }

                            if (message.contains("send")) {
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
            };
            th1.start();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}



