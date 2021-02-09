package clientserver.commands;

import clientserver.Command;
import clientserver.Commands;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.CharsetUtil;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class CommandLS implements Command {
    public static byte[] makeResponse(List<String> listFile) {
        //[команда 1б][длина сообщения 4б][кол объектов 4 байта][длина имени1 4б][имя1][длина имени2 4б][имя2]...
        int lengthResponse = (listFile.size()+2)*4+1;
        for (String s : listFile) {
            lengthResponse += s.getBytes(StandardCharsets.UTF_8).length;
        }
        // 1 команда
        byte[] response = new byte[lengthResponse];
        response[0] = Commands.LS.getSignal();
        // 2 длина сообщения
        byte[] arrayCount = ByteBuffer.allocate(4).putInt(lengthResponse).array();
        System.arraycopy(arrayCount, 0, response, 1, arrayCount.length);

        // 3 кол объектов
        int count = listFile.size();
        arrayCount = ByteBuffer.allocate(4).putInt(count).array();
        System.arraycopy(arrayCount, 0, response, 5, arrayCount.length);

        //4 имена
        int i = 9;
        for (String s : listFile) {
            arrayCount = ByteBuffer.allocate(4).putInt(s.getBytes().length).array();
            System.arraycopy(arrayCount, 0, response, i, arrayCount.length);
            i += arrayCount.length;
            arrayCount = s.getBytes(StandardCharsets.UTF_8);
            System.arraycopy(arrayCount, 0, response, i, arrayCount.length);
            i += arrayCount.length;
        }

        return response;
    }

    public static List<String> readResponse(ByteBuf buf) {
        buf.readInt();
        buf.readInt();
        List<String> list = new ArrayList<>();
        while (buf.isReadable()) {
            list.add(buf.readBytes(buf.readInt()).toString(CharsetUtil.UTF_8));
        }
        return list;
    }

    @Override
    public void apply(ChannelHandlerContext ctx, String content, byte signal) {
        ByteBuf byBuf = ByteBufAllocator.DEFAULT.buffer();
        byBuf.writeByte(signal);
        byBuf.writeInt(5);
        ctx.writeAndFlush(byBuf);
    }
}
