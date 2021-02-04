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
import java.nio.charset.StandardCharsets;
import java.util.List;

public class InboundHandler extends ChannelInboundHandlerAdapter {
    private String storageDir = "storage";
    private String userName = "user_1";
    private ByteBuf bufSum;

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        bufSum = ctx.alloc().buffer(1024*1024);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        bufSum.release();
        bufSum = null;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Кто-то подключился");
        ctx.fireChannelRegistered();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        bufSum.writeBytes(buf);
        buf.release();

//        System.out.println(buf.toString(StandardCharsets.UTF_8));
        System.out.println("пришло сообщение");
        assert buf != null;
        if (buf.readableBytes() < 1) {
            buf.release();
            ctx.writeAndFlush("Неверная команда");  // отправка сообщения
        } else {

            byte b = buf.readByte();
            Commands command = Commands.getCommand(b);
//            System.out.println("Получена команда: " + command);
            if (command != null) {
                switch (command) {
                    case LS:  // команда LS
                        // читаем список директорий и файлов в текущей диретории
                        byte[] answer;
                        answer = CommandLS.makeResponse(List.of("file1.txt", "file2.txt"));
//                        System.out.println("Создан ответ: "+answer);
                        // отправляем этот список
                        ByteBuf bufOut = ctx.alloc().buffer(answer.length);
                        bufOut.writeBytes(answer);
                        ctx.writeAndFlush(bufOut);
                        break;
                    case UPLOAD:
                        String dir = storageDir + File.separator + userName;
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
