package ru.daniilazarnov;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
        //Почему path не видел файл при указании такого пути: src/main/resources/test.txt ?
//        Path path = Paths.get("project/client/src/main/resources/testFile.txt");
        Path path = Paths.get("project/client/src/main/resources/exceptions.png");
        SocketChannel sc = SocketChannel.open(new InetSocketAddress(host, port));
//        System.out.println(path.toFile().exists());
//        System.out.println(path.toFile().length());
        String fileName = "exceptions.png";

        int size = (int)(path.toFile().length());
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        byteBuffer.put((byte) 1);//номер команды
        byteBuffer.flip();
        sc.write(byteBuffer);
        byteBuffer.clear();

        byteBuffer.putInt(size);//размер файла
        byteBuffer.putInt(fileName.getBytes().length);//длина названия файла
        byteBuffer.put(fileName.getBytes());//название файла
        byteBuffer.flip();
        sc.write(byteBuffer);
        byteBuffer.clear();

        byteBuffer.flip();
        sc.write(byteBuffer);
        byteBuffer.clear();


        FileInputStream is = new FileInputStream("project/client/src/main/resources/exceptions.png");
        FileChannel fileChannel = is.getChannel();
//        fileChannel.transferTo(0, fileChannel.size(), sc.socket().getChannel());


//        FileChannel fileChannel = FileChannel.open(path1);    //is.getChannel();
        int flag = fileChannel.read(byteBuffer);
        while(flag != 0) {
            byteBuffer.flip();
            sc.write(byteBuffer);
            byteBuffer.clear();
            flag = fileChannel.read(byteBuffer);
        }
        sc.write(byteBuffer);
        System.out.println(Files.exists(path));
        byte[] arr = {1,2,3,4,5,6,7,8,9,8,7,6,5,4,3,2,1,2,3,4,5,6,7,8,9};
        sc.write(ByteBuffer.wrap(arr));
//        while (sc.isConnected()) {
//            byte[] arr = new byte[]{0,0,0,4,5,6,7};
//            sc.write(ByteBuffer.wrap(arr));
//        }
        return true;
    }
}
