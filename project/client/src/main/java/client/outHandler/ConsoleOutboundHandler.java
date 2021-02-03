package client.outHandler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import clientserver.Commands;
import clientserver.commands.CommandLS;

import java.net.SocketAddress;
import java.util.Scanner;

public class ConsoleOutboundHandler extends ChannelOutboundHandlerAdapter {
    @Override
    public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) throws Exception {
        System.out.print("Введите команду: ");

        ctx.connect(remoteAddress, localAddress, promise);

        Scanner in = new Scanner(System.in);
        StringBuilder readLine = new StringBuilder();
        if (in.hasNext()) {
            readLine.append(in.next());
            System.out.println("append ");
        }
        ByteBuf by = ByteBufAllocator.DEFAULT.buffer();
        String command = readLine.substring(0,2);
        System.out.println("команда "+command);
        switch (command) {
            case "ls":
                by.writeByte(Commands.LS.getSignal());
                System.out.println("case ls:"+by.getByte(0));
                break;
            default:
                System.out.println("Неизвестная команда");
        }
        System.out.println("end switch"+remoteAddress.toString());
        ctx.writeAndFlush(by);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
//        System.out.print("write: ");
//        Scanner in = new Scanner(System.in);
//        StringBuilder readLine = new StringBuilder();
//        if (in.hasNext()) {
//            readLine.append(in.next());
//        }
//        ByteBuf by = ByteBufAllocator.DEFAULT.buffer();
//        String command = readLine.substring(0,readLine.indexOf(" "));
//        switch (command) {
//            case "ls":
//                by.writeByte(Commands.LS.getSignal());
//                break;
//            default:
//                System.out.println("Неизвестная команда");
//        }
        ctx.writeAndFlush(msg);
    }
}
