package server.storage.inHandler;

import clientserver.Commands;
import clientserver.commands.CommandLS;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.List;

public class InboundHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Кто-то подключился");
        ctx.fireChannelRegistered();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("1. First");
        ByteBuf buf = null;
        if (msg instanceof ByteBuf) {
            buf = (ByteBuf) msg;
        }
        System.out.println(buf);

        if (buf.readableBytes() < 1) {
            buf.release();
            ctx.writeAndFlush("Неверная команда");  // отправка сообщения
        } else {

            byte b = buf.readByte();
            Commands command = Commands.getCommand(b);
//            String str = buf.toString(CharsetUtil.UTF_8);
            System.out.println("Получена команда: " + command);
            byte[] answer;
            if (command != null) {
                switch (command) {
                    case LS:  // команда LS
//                    System.out.println("LS");
                        // читаем список директорий и файлов в текущей диретории
                        answer = CommandLS.makeResponse(List.of("file1.txt", "file2.txt"));
                        System.out.println("Создан ответ: "+answer);
                        // отправляем этот список
                        buf.writeBytes(answer);
                        ChannelFuture transferOperationFuture = ctx.writeAndFlush(buf);
                        transferOperationFuture.addListener(new ChannelFutureListener() {
                            @Override
                            public void operationComplete(ChannelFuture future) {
                                assert transferOperationFuture == future;
                                System.out.println("Ответ передан!");
                            }
                        });
//                        ctx.writeAndFlush(answer);
                        break;
                    case CD:
                        System.out.println("CD");
                        break;
                }
            }
        }
//        ctx.fireChannelRead(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
