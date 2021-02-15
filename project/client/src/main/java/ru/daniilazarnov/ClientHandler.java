package ru.daniilazarnov;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;

public class ClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx){
        Thread t1 = new Thread(() ->{
            while (true){
                ClientCom com = new ClientCom();
                Scanner scanner = new Scanner(System.in);
                String msg = scanner.nextLine();
                switch (msg) {
                    case "*send":
                        com.send(ctx);
                        break;
                    case "*show":
                        com.showFiles(ctx);
                        break;
                    case "*exit":
                        System.out.println("Соединение разорвано...");
                        ctx.close();
                        System.exit(0);
                        break;
                    case "*dl":
                        com.download(ctx);
                        break;
                    default:
                        System.out.println("Неизвестная комманда");
                }
            }
        });
        t1.start();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof FileMessage){
            System.out.println("Скачивание файла...");
            try {
                Files.write(Path.of("C:\\Users\\Роман\\Desktop\\java-net-course\\project\\client\\src\\Files"
                                + File.pathSeparator + ((FileMessage) msg).getFileName()),
                        ((FileMessage) msg).getContent(),
                        StandardOpenOption.CREATE_NEW);
                System.out.println("Файл скачан!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else System.out.println("Сообщение от сервера: " + msg);
    }
}
