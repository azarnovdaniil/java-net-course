package ru.daniilazarnov;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

class Main2 {
    public static void main(String[] args) throws IOException, URISyntaxException {
        Client cl = new Client("localhost", 8199);
        cl.connect();
    }
}


public class Client {

    private final int port;
    private final String host;
    private Path currentDirectory;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }



    public boolean connect() throws IOException {
        Path path = Paths.get("project/client/src/main/resources/exceptions.png");
        FileProtocol.connect(new InetSocketAddress(host, port));
        Scanner scanner = new Scanner(System.in);
        String message = scanner.nextLine();
        FileProtocol.trueSender(message);
//        FileProtocol.sender(sc, path);
//        String fileName = "exceptions.png";
//
//        int size = (int)(path.toFile().length());
//        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
//
//        byteBuffer.put((byte) 1);//номер команды
//        byteBuffer.flip();
//        sc.write(byteBuffer);
//        byteBuffer.clear();
//
//        byteBuffer.putInt(size);//размер файла
//        byteBuffer.putInt(fileName.getBytes().length);//длина названия файла
//        byteBuffer.put(fileName.getBytes());//название файла
//        byteBuffer.flip();
//        sc.write(byteBuffer);
//        byteBuffer.clear();
//
//        byteBuffer.flip();
//        sc.write(byteBuffer);
//        byteBuffer.clear();
//
//
//        FileInputStream is = new FileInputStream("project/client/src/main/resources/exceptions.png");
//        FileChannel fileChannel = is.getChannel();
//
//        int flag = fileChannel.read(byteBuffer);
//        while(flag != 0) {
//            byteBuffer.flip();
//            sc.write(byteBuffer);
//            byteBuffer.clear();
//            flag = fileChannel.read(byteBuffer);
//        }
//        sc.write(byteBuffer);

        return true;
    }
}
