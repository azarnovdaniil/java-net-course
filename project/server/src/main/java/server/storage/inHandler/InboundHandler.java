package server.storage.inHandler;

import clientserver.Commands;
import clientserver.MakeCommand;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.util.CharsetUtil;

import java.util.ArrayList;
import java.util.List;

public class InboundHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("1. First");
        ByteBuf buf = (ByteBuf) msg;

        if (buf.readableBytes() < 1) {
            buf.release();
            ctx.writeAndFlush("Неверная команда");  // отправка сообщения
        }

        Commands command = Commands.getCommand(buf.getByte(0));
        String str = buf.toString(CharsetUtil.UTF_8);
        System.out.println(str + " команда: " + command + " name=" + command.name());
        if (command != null) {
            switch (command) {
                case LS:  // команда LS
                    System.out.println("LS");
                    // читаем список директорий и файлов в текущей диретории

                    MakeCommand.CommandDataLSResponse(List.of("file1.txt","file2.txt"));

                    // отправляем этот список
                    //ctx.writeAndFlush();
                    break;
                case CD:
                    System.out.println("CD");
                    break;
                default:
                    break;
            }
            ctx.writeAndFlush("Получена команда " + command);
        }
//        ctx.fireChannelRead(msg);
    }
}
