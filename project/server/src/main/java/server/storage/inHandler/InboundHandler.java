package server.storage.inHandler;

import clientserver.Commands;
import clientserver.commands.CommandLS;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class InboundHandler extends ChannelInboundHandlerAdapter {
    private String storageDir = "storage";
    private String userName = "user_1";
    private String currentDir;

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Подключился клиент "+ctx.channel().remoteAddress().toString());
        currentDir = storageDir + File.separator + userName;
        ctx.fireChannelRegistered();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;

        System.out.print("пришло сообщение ");
        System.out.println(buf.readableBytes());
        assert buf != null;
        if (buf.readableBytes() < 1) {
            buf.release();
            ctx.writeAndFlush("Неверная команда");  // отправка сообщения
        } else {

            byte b = buf.readByte();
            Commands command = Commands.getCommand(b);
            if (command != null) {
                switch (command) {
                    case LS:  // команда LS
                        // читаем список директорий и файлов в текущей диретории
                        byte[] answer;
                        List<String> filesInDir = Files.list(Path.of(currentDir))
                                                    .map(Path::toFile)
                                                    .map(File::getName)
                                                    .collect(toList());
                        answer = CommandLS.makeResponse(filesInDir);
//                        System.out.println("Создан ответ: "+answer);
                        // отправляем этот список
                        ByteBuf bufOut = ctx.alloc().buffer(answer.length);
                        bufOut.writeBytes(answer);
                        ctx.writeAndFlush(bufOut);
                        break;
                    case UPLOAD:
                        String dir = storageDir + File.separator + userName;
//                        buf.readLong(); // длина всего сообщения
                        buf.readInt(); // длина всего сообщения
                        int hash = buf.readInt();
                        int partNum = buf.readInt();
                        int fileNameSize = buf.readInt();
                        ByteBuf fileNameBuf = buf.readBytes(fileNameSize);
                        String fileName = fileNameBuf.toString(StandardCharsets.UTF_8);
                        long fileSize = buf.readLong();

                        File file = new File(dir + File.separator + fileName);
                        if (!file.exists()) file.createNewFile();
                        FileOutputStream fileOutputStream = new FileOutputStream(file);
                        byte[] bufIn = new byte[(int)fileSize];
                        int writeSize = buf.readableBytes();
//                        bufIn =
                        buf.readBytes(fileOutputStream, writeSize);
//                        fileOutputStream.write(buf.readBytes((int) fileSize).array());
                        fileOutputStream.flush();
                        fileOutputStream.close();
                        System.out.println("Сохранен файл " + fileName);
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
