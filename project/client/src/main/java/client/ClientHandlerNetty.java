package client;

import clientserver.Commands;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Locale;
import java.util.Scanner;

public class ClientHandlerNetty extends SimpleChannelInboundHandler {
    private int partSize;

    public ClientHandlerNetty(int partSize) {
        super();
        this.partSize = partSize;
    }

    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext) {
//        channelHandlerContext.writeAndFlush(Unpooled.copiedBuffer("Netty Rocks!", CharsetUtil.UTF_8));
        consoleRead(channelHandlerContext);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        System.out.println("Пришло сообщение");

        ByteBuf buf = (ByteBuf) o;
        if (buf.readableBytes() > 0) {
            byte firstByte = buf.readByte();
            Commands command = Commands.getCommand(firstByte);
            command.receive(channelHandlerContext, buf);
        }
        consoleRead(channelHandlerContext);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable cause) {
        cause.printStackTrace();
        channelHandlerContext.close();
    }

    public void consoleRead(ChannelHandlerContext ctx) {
//        Thread threadConsole = new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
//            while (true) {
                String readLine = "";
                System.out.print("Введите команду: ");
                if (scanner.hasNext()) {
                    readLine = scanner.nextLine();
                    String[] commandPart = readLine.split("\\s", 2);
                    Commands command = Commands.valueOf(commandPart[0].toUpperCase(Locale.ROOT));
                    String commandParam = commandPart.length > 1 ? commandPart[1] : commandPart[0];

                    command.sendToServer(ctx, commandParam);
//                        TimeUnit.SECONDS.sleep(2);
                }
//            }
//        });
//        threadConsole.setDaemon(true);
//        threadConsole.start();
    }
}
