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
        Socket s = new Socket("localhost", 8189);
        os = s.getOutputStream();
        InputStream is = s.getInputStream();
//        DataInputStream dis = new DataInputStream(is);
        Scanner scanner = new Scanner(is);
        consoleRead();
        byte[] b = new byte[10000];
        byte firstB = 0;
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
        while (!s.isClosed()) {
//            buf.release();
            if (scanner.hasNext()) {
//            int readByte;
//            readByte = dis.readByte();
//            if (readByte!=-1) {
//                dis
//                System.out.println(scanner.next());
//                String otvet = scanner.next();
                System.out.println("Прочитано: ");
                while (scanner.hasNext()) {
                    buf.writeBytes(scanner.next().getBytes(StandardCharsets.UTF_8));
                }
                System.out.println(buf.toString(StandardCharsets.UTF_8));
                if (buf!= null) {
                    firstB = buf.readByte();
                    if (firstB == (byte) 3) {
                        List<String> list = CommandLS.readResponse(buf);
                        System.out.println(list);
                    }
                }
            }
        }

    }

    private static void consoleRead() throws IOException {
        Thread threadSout = new Thread(() -> {
            try {
                Scanner scanner = new Scanner(System.in);
                StringBuilder readLine = new StringBuilder();
                while (true) {
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
