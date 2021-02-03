package client;

import clientserver.Command;
import clientserver.Commands;
import clientserver.commands.CommandLS;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

public class NettyClient {
    private static OutputStream os;

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 8189);
        os = socket.getOutputStream();
        byte[] by = new byte[]{(byte) 3};
        os.write(by);
        os.flush();
        InputStream is = socket.getInputStream();
        DataInputStream dis = new DataInputStream(is);
//        Scanner scanner = new Scanner(is);
//        consoleRead();
        os.write(by);
        byte[] b = new byte[10000];
        byte firstB = 0;
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
        StringBuilder response = new StringBuilder();
        while (!socket.isClosed()) {
            int maxLen = 1024;
            byte[] contextBytes = new byte[maxLen];
            int readLen;
            while ((readLen = dis.read(contextBytes, 0, maxLen)) != -1) {
                response.append(new String(contextBytes, 0, readLen));
                readLen = dis.read(contextBytes, 0, maxLen);
            }

////            buf.release();
            String scan;
////            if (scanner.hasNext()) {
//            scan = dis.readUTF();

//            System.out.println("Прочитано0: " + firstB);
////
////            while (readByte != -1) {
////                dis
////                System.out.println(scanner.next());
////                String otvet = scanner.next();
//            buf.writeByte(firstB);
//            int readByte = dis.readInt();
//            System.out.println("Прочитано: " + readByte);
//                firstB = scanner.nextByte();
//
//                System.out.println("Прочитано1: " + firstB);
//                buf.writeByte(firstB);
//                int secInt = scanner.nextInt();
//                System.out.println("Прочитано2: " + secInt+" еще:"+scanner.hasNext());
//                buf.writeInt(secInt);
//                secInt = scanner.nextInt();
//                System.out.println("Прочитано3: " + secInt+" еще:"+scanner.hasNext());
//                buf.writeInt(secInt);
//            }


            System.out.println("while закончен: " + response);
//            System.out.println("while закончен: " + buf.toString(StandardCharsets.UTF_8));
            if (buf != null) {
                System.out.println("buf!=null");
                firstB = buf.readByte();
                System.out.println(firstB);
                if (firstB == (byte) 3) {
                    List<String> list = CommandLS.readResponse(buf);
                    System.out.println("файлы: "+ list);
                }
            }
        }
    }

    private static void consoleRead() throws IOException {
        Thread threadSout = new Thread(() -> {
            try {
                Scanner scanner = new Scanner(System.in);
                while (true) {
                    StringBuilder readLine = new StringBuilder();

                    System.out.print("Введите команду: ");

                    if (scanner.hasNext()) {
                        readLine.append(scanner.next());
                    }
                    byte[] by = new byte[100];

                    String command = readLine.substring(0, 2);
                    System.out.println("команда " + command);
                    switch (command) {
                        case "ls":
                            by[0] = Commands.LS.getSignal();
                            os.write(by);
                            os.flush();
                            System.out.println("case ls:" + by[0]);
                            break;
                        default:
                            System.out.println("Неизвестная команда");
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        threadSout.setDaemon(true);
        threadSout.start();
    }
}
