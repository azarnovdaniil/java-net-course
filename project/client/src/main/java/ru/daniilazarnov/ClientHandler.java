package ru.daniilazarnov;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.util.Locale;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;


public class ClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext) {
        consoleRead(channelHandlerContext);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable cause) {
        cause.printStackTrace();
        channelHandlerContext.close();
    }

    public void consoleRead(ChannelHandlerContext ctx) {
        Thread threadConsole = new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String readLine = "";
                System.out.print("Введите команду: ");
                if (scanner.hasNext()) {
                    readLine = scanner.nextLine();
                    Commands command = Commands.valueOf(readLine.toUpperCase(Locale.ROOT));
                    command.sendToServer(ctx);
                    try {
                        TimeUnit.SECONDS.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        threadConsole.setDaemon(true);
        threadConsole.start();
    }
}
