package clientserver.commands;

import clientserver.Commands;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.util.CharsetUtil;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class CommandLS {
    public static byte[] makeResponse(List<String> listFile) {
        //[команда 1б][кол объектов 4 байта][длина имени1 4б][имя1][длина имени2 4б][имя2]...
        int lengthResponse = (listFile.size()+1)*4+1;
        for (String s : listFile) {
            lengthResponse += s.getBytes(StandardCharsets.UTF_8).length;
        }
        // 1 команда
        byte[] response = new byte[lengthResponse];
        response[0] = Commands.LS.getSignal();
        // 2 кол объектов
        int count = listFile.size();
        byte[] arrayCount = ByteBuffer.allocate(4).putInt(count).array();
        System.arraycopy(arrayCount, 0, response, 1, arrayCount.length);

        //3 имена
        int i = 5;
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
//        ByteBuf buf = ByteBufAllocator.DEFAULT.directBuffer(response.length);
//        buf.writeBytes(response);
//        System.out.println(buf.toString(CharsetUtil.UTF_8));
//        buf.readByte();
        int count = buf.readInt();
        List<String> list = new ArrayList<>();
        while (buf.isReadable()) {
            list.add(buf.readBytes(buf.readInt()).toString(CharsetUtil.UTF_8));
        }

        return list;
    }
}
