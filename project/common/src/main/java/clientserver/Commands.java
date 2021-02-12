package clientserver;

import clientserver.commands.*;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.CharsetUtil;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum Commands {
    UPLOAD("upload", (byte) 1, new CommandUpload()),
    LS("ls", (byte) 2, new CommandLS()),
    DOWNLOAD("download", (byte) 3, new CommandDownload()),
    //    RM("rm", (byte) 4),
//    MKDIR("mkdir", (byte) 5),
//    MV("mv", (byte) 6),
//    CD("cd", (byte) 7),
//    RENAME("rename", (byte) 8),
    UNKNOWN("unknown", Byte.MIN_VALUE, new CommandUnknown());

    private static final Map<Byte, Commands> commandsMap = Arrays.stream(Commands.values())
            .collect(Collectors.toMap(commands -> commands.signal, Function.identity()));
    private final byte signal;
    private final String name;
    private final Command commandApply;

    Commands(String name, byte signal, Command command) {
        this.name = name;
        this.signal = signal;
        this.commandApply = command;
    }

    public void sendToServer(ChannelHandlerContext ctx, String readLine) {
        commandApply.send(ctx, readLine, signal);
    }

    public void receiveAndSend(ChannelHandlerContext ctx, ByteBuf buf, String currentDir, Map<Integer, FileLoaded> uploadedFiles) {
        commandApply.response(ctx, buf, currentDir, uploadedFiles, signal);
    }

    public void receive(ChannelHandlerContext ctx, ByteBuf buf, String currentDir, Map<Integer, FileLoaded> uploadedFiles) {
        commandApply.receive(ctx, buf, currentDir, uploadedFiles);
    }

    public static Commands getCommand(byte code) {
        return commandsMap.getOrDefault(code, UNKNOWN);
    }

    public static byte[] makeArrayFromList(List<String> listFile) {
        int lengthResponse = (listFile.size() + 2) * 4 + 1;
        for (String s : listFile) {
            lengthResponse += s.getBytes(StandardCharsets.UTF_8).length;
        }

        byte[] response = new byte[lengthResponse];
        response[0] = 0;
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
