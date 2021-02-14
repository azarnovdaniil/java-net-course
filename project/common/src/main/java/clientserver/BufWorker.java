package clientserver;

import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class BufWorker {
    public static byte[] makeArrayFromList(List<String> listFile) {
        int lengthResponse = (listFile.size() + 2) * 4 + 1;
        for (String s : listFile) {
            lengthResponse += s.getBytes(StandardCharsets.UTF_8).length;
        }

        byte[] response = new byte[lengthResponse];
        response[0] = 0;
        byte[] arrayCount = ByteBuffer.allocate(4).putInt(lengthResponse).array();
        System.arraycopy(arrayCount, 0, response, 1, arrayCount.length);

        int count = listFile.size();
        arrayCount = ByteBuffer.allocate(4).putInt(count).array();
        System.arraycopy(arrayCount, 0, response, 5, arrayCount.length);

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
