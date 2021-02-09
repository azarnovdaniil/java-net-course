package clientserver.commands;

import clientserver.Command;
import clientserver.Commands;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class CommandUpload implements Command {
    public static int getPartSize() {
        return partSize;
    }

    private static final int partSize = 5*1024 * 1024;

    public static byte[] makeResponse(String fileName) {
        //[команда 1б][длина сообщения 4б][хэш 4б][номер части 4б][длина имени 4б][имя][размер файла 8б][содержимое]
        //если номер части =-1, значит она одна
        File file = new File(fileName);
        if (!file.exists()) return null;
        String name = file.getName();
        int hash = getHash(file);
        int partNum;
        int lengthResponse;
        long volume = file.length();
        int countPar = (int) volume / partSize + 1;
        partNum = countPar == 1 ? -1 : 1;
        lengthResponse = 4 + 4 + 4 + name.getBytes(StandardCharsets.UTF_8).length + 8;
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer(lengthResponse);

//        buf.writeByte(Commands.UPLOAD.getSignal()); // 1 команда
        buf.writeInt(hash); // 2 хэш
        buf.writeInt(partNum); // 3 номер части
        buf.writeInt(name.getBytes(StandardCharsets.UTF_8).length); // 4 длина имени
        buf.writeBytes(name.getBytes(StandardCharsets.UTF_8)); // 5 имя
        buf.writeLong(volume);

        byte[] bufOut = new byte[lengthResponse];
        buf.readBytes(bufOut, 0, lengthResponse);
        return bufOut;
}


    private static int getHash(File file) {
        return file.hashCode();
    }


    @Override
    public void apply(ChannelHandlerContext ctx, String content, byte signal) {

    }
}