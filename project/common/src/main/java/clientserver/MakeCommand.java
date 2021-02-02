package clientserver;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.util.CharsetUtil;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class MakeCommand {
    public static byte[] CommandDataLSResponse(List<String> listFile) {
        //[команда][кол объектов 3 байта][длина имени1][имя1][длина имени2][имя2]...
        byte[] array = new byte[50];
        array[0] = Commands.LS.getSignal();
        int count = listFile.size();
        ByteBuf arrayCount; // = ByteBuffer.allocate(3).putInt(count).array();
        arrayCount = ByteBufAllocator.DEFAULT.directBuffer(3);

        arrayCount.writeInt(count);
        System.out.println(arrayCount.toString(CharsetUtil.UTF_8));
        return array;
    }
}
