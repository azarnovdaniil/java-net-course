package ru.daniilazarnov;

import io.netty.channel.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ServerHandler extends SimpleChannelInboundHandler<String> {

    private static final List<Channel> CHANNELS = new ArrayList<>();
    private static int newClientIndex = 1;
    private String clientName;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Клиент подключился" + ctx);
        CHANNELS.add(ctx.channel());
        clientName = "Клиент №" + newClientIndex;
        newClientIndex++;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        System.out.println("Получено сообщение: " + s);
        String out;
        //String out = String.format("%s\n", s);

        for (Channel c: CHANNELS) {
            // ls + имя папки - вывести имена всех файлов папки
            if (s.contains("ls")) {
               String nameDirectory = s.replaceAll("ls ", "");
                File dir = new File(nameDirectory);
                File[] arrFiles = dir.listFiles();
                List<File> lst = Arrays.asList(arrFiles);
                out = String.valueOf(lst);
                c.writeAndFlush("\n" + out);
            }
            // create имя папки/имя нового файла - создать новый файл в конкретной папке
            if (s.contains("create")) {
                String directoryAndFile = s.replaceAll("create ", "");
                Path path = Paths.get(directoryAndFile);
                Path newFile = Files.createFile(path);
                c.writeAndFlush("\nNew file was created");
            }
            // delete имя папки/имя файла, который нужно удалить - удалить файл
            if (s.contains("delete")) {
                String directoryAndFile = s.replaceAll("delete ", "");
                Path rootPath = Paths.get(directoryAndFile);
                Files.walkFileTree(rootPath, new SimpleFileVisitor<Path>() {
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        System.out.println("delete file: " + file.toString());
                        Files.delete(file);
                        return FileVisitResult.CONTINUE;
                    }
                });
                c.writeAndFlush("\nFile was deleted");
            }
            // sendMsg имя папки/имя файла/сообщение
            if (s.contains("sendMsg ")) {
                s = s.replaceAll("sendMsg ", "");
                String[] strArray = s.split("/");
                String nameDirectory = strArray[0];
                String nameFile = strArray[1];
                String msg = strArray[2];
                try {
                    Path rootPath = Paths.get(nameDirectory);
                    String fileToFind = File.separator + nameFile;

                    Files.walkFileTree(rootPath, new SimpleFileVisitor<Path>() {
                        @Override
                        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                            String fileString = file.toAbsolutePath().toString();
                            if (fileString.endsWith(fileToFind)) {
                                Path way = file.toAbsolutePath();
                                PrintWriter out3 = new PrintWriter(new FileWriter(String.valueOf(way)), true);
                                out3.println(msg);
                                return FileVisitResult.TERMINATE;
                            }
                            return FileVisitResult.CONTINUE;
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
                c.writeAndFlush("\nMessage was written");
            }
        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
