package ru.atoroschin;

import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class BufWorker {
    public static byte[] makeArrayFromList(List<String> listFile) {
        final int countFour = 4;
        final int countTwo = 2;
        int lengthResponse = (listFile.size() + countTwo) * countFour + 1;
        for (String s : listFile) {
            lengthResponse += s.getBytes(StandardCharsets.UTF_8).length;
        }

        byte[] response = new byte[lengthResponse];
        response[0] = 0;
        byte[] arrayCount = ByteBuffer.allocate(countFour).putInt(lengthResponse).array();
        System.arraycopy(arrayCount, 0, response, 1, arrayCount.length);

        int count = listFile.size();
        final int countFive = 5;
        arrayCount = ByteBuffer.allocate(countFour).putInt(count).array();
        System.arraycopy(arrayCount, 0, response, countFive, arrayCount.length);

        final int i = 9;
        int sum = i;
        for (String s : listFile) {
            arrayCount = ByteBuffer.allocate(countFour).putInt(s.getBytes().length).array();
            System.arraycopy(arrayCount, 0, response, sum, arrayCount.length);
            sum += arrayCount.length;
            arrayCount = s.getBytes(StandardCharsets.UTF_8);
            System.arraycopy(arrayCount, 0, response, sum, arrayCount.length);
            sum += arrayCount.length;
        }
        return response;
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
