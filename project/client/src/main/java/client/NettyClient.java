package client;

import clientserver.Commands;
import clientserver.commands.CommandLS;
import clientserver.commands.CommandUpload;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class NettyClient {
    private static OutputStream outputStream;
    private static InputStream inputStream;
    private static final Semaphore smp = new Semaphore(1);


    public static void main(String[] args) throws IOException, InterruptedException {
        Socket socket = new Socket("localhost", 8189);
        outputStream = socket.getOutputStream();
        inputStream = socket.getInputStream();
        consoleRead();
        byte firstB = 0;
        ByteBuf buf;
        while (!socket.isClosed()) {
            buf = readInput();
            smp.acquire();
            firstB = buf.readByte();
            switch (firstB) {
                case 1:
                    // команда upload
                    break;
                case 3:
                    List<String> list = CommandLS.readResponse(buf);
                    System.out.println("файлы: " + list);
                    break;
                case 4:
                    break;
            }
            smp.release();
        }
    }

    private static void consoleRead() {
        Thread threadSout = new Thread(() -> {
            try {
                Scanner scanner = new Scanner(System.in);
                while (true) {
                    String readLine;
                    smp.acquire();
                    System.out.print("Введите команду: ");
                    if (scanner.hasNext()) {
                        readLine = scanner.next();
                        smp.release();
                        String command = readLine.contains(" ") ? readLine.split(" ")[0] : readLine;
                        readLine = scanner.next();
                        byte[] by;
                        switch (command) {
                            case "upload":
                                String fileName = readLine.contains(" ") ? readLine.split(" ")[0] : readLine;
                                // запаковать в массив
                                by = CommandUpload.makeResponse(fileName);
                                if (by != null) ClientHandler.uploadFile(outputStream, fileName, by);
//                                outputStream.write(by);
//                                outputStream.flush();
                                break;
                            case "ls":
                                by = new byte[1];
                                by[0] = Commands.LS.getSignal();
                                outputStream.write(by);
                                outputStream.flush();
                                break;
                            default:
                                System.out.println("Неизвестная команда");
                        }
                        Thread.sleep(2 * 1000);
                    }
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });
        threadSout.setDaemon(true);
        threadSout.start();
    }

    private static ByteBuf readInput() throws IOException {
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
        int maxLen = 1024 * 100;
        byte[] contextBytes = new byte[maxLen];
        int readLen;
        readLen = inputStream.read(contextBytes, 0, maxLen);
        while (readLen == maxLen) {
            buf.writeBytes(contextBytes);
            readLen = inputStream.read(contextBytes, 0, maxLen);
        }
        buf.writeBytes(contextBytes, 0, readLen);
        return buf;
    }
}
