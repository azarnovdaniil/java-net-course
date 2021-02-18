package ru.atoroschin;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.util.CharsetUtil;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class BufWorker {
    /**
     * метод подготавливает сообщение из списка строк
     * формат сообщения в виде массива байт
     * [команда 1б][длина сообщения 4б][кол строк 4б][длина стр1][стр1][длина стр2][стр2][...][...]
     *
     * @param listFile - список строк для передачи
     * @return массив ByteBuf
     */
    public static ByteBuf makeBufFromList(List<String> listFile, int signal) {
        final int countByteInInt = 4;
        int lengthResponse = (listFile.size() + 1 + 1) * countByteInInt + 1;
        for (String s : listFile) {
            lengthResponse += s.getBytes(StandardCharsets.UTF_8).length;
        }

        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer(lengthResponse);
        buf.writeByte(signal); // 1
        buf.writeInt(lengthResponse); // 2
        int count = listFile.size();
        buf.writeInt(count); // 3
        for (String s : listFile) {
            buf.writeInt(s.getBytes().length); // 4
            buf.writeBytes(s.getBytes(StandardCharsets.UTF_8)); // 5
        }
        return buf;
    }

    public static List<String> readFileListFromBuf(ByteBuf buf) {
        buf.readInt();
        buf.readInt();
        List<String> list = new ArrayList<>();
        while (buf.isReadable()) {
            list.add(buf.readBytes(buf.readInt()).toString(CharsetUtil.UTF_8));
        }
        return list;
    }
}
