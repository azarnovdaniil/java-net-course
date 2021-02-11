package client;

import clientserver.Commands;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Locale;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class ClientHandlerNetty extends ChannelInboundHandlerAdapter { //SimpleChannelInboundHandler
    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext) {
        consoleRead(channelHandlerContext);
    }

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        if (buf.readableBytes() > 0) {
            byte firstByte = buf.readByte();
            Commands command = Commands.getCommand(firstByte);
            command.receive(channelHandlerContext, buf);
        }
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
                    String[] commandPart = readLine.split("\\s", 2);
                    Commands command = Commands.valueOf(commandPart[0].toUpperCase(Locale.ROOT));
                    String commandParam = commandPart.length > 1 ? commandPart[1] : commandPart[0];

                    command.sendToServer(ctx, commandParam);
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
