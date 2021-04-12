package ru.daniilazarnov;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ru.daniilazarnov.commands.Command;
import ru.daniilazarnov.commands.DownLoadFile;
import ru.daniilazarnov.commands.UpLoadFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ClientHandler extends ChannelInboundHandlerAdapter {
    Map<String, Command> mapCommands = new HashMap<String, Command>();
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    DownLoadFile downLoadFile = new DownLoadFile() ;
    UpLoadFile upLoadFile = new UpLoadFile();


    ClientHandler() {
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Thread t = new Thread(() -> {
            while (true) {
                Scanner scanner = new Scanner(System.in);
                String msg = scanner.nextLine();
                String[] command = msg.split("\\s");
                if(msg.equals("exit"))
                {
                    Network.getInstance().stop();
                    break;
                }
                Path pathFile = Path.of(command[1]);
                if (command[0].equals("upload")) {
                    try {
                        //C:\Users\Stas\Desktop\Java.NET\java-net-course\project\client\src\main\java\ru\daniilazarnov\demo.txt
                        upLoadFile.sendFile(pathFile, Network.getInstance().getCurrentChannel(), future -> {
                            if (!future.isSuccess()) {
                                future.cause().printStackTrace();
                                //                Network.getInstance().stop();
                            }
                            if (future.isSuccess()) {
                                System.out.println("Файл успешно передан");
                                //                Network.getInstance().stop();
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (command[0].equals("download")) {
                    try {
                        downLoadFile.downloadRequest(pathFile, Network.getInstance().getCurrentChannel(), future -> {
                            if (!future.isSuccess()) {
                                future.cause().printStackTrace();
                            }
                            if (future.isSuccess()) {
                                System.out.println("Запрос на загрузку отправлен");
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

            }



            }

        });
        t.start();
        t.isAlive();


    }
}
