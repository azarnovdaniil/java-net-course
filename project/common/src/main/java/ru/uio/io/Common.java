package ru.uio.io;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class Common {
    private Common() {
    }

    public static byte[] readStream(DataInputStream din) {
        byte[] dataBuff = null;
        try {
            int b = 0;
            String buffLength = "";
            while ((b = din.read()) != getNumber("read")) {
                buffLength += (char) b;
            }
            int dataLength = Integer.parseInt(buffLength);
            dataBuff = new byte[Integer.parseInt(buffLength)];
            int byteRead = 0;
            int byteOffset = 0;
            while (byteOffset < dataLength) {
                byteRead = din.read(dataBuff, byteOffset, dataLength - byteOffset);
                byteOffset += byteRead;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return dataBuff;
    }

    public static byte[] createDataPacket(byte[] cmd, byte[] data) {
        byte[] packet = null;
        byte[] initialize = new byte[1];
        initialize[0] = 2;
        byte[] separator = new byte[1];
        separator[0] = 4;
        byte[] dataLength = String.valueOf(data.length).getBytes(StandardCharsets.UTF_8);
        packet = new byte[initialize.length + cmd.length + separator.length + dataLength.length + data.length];
        System.arraycopy(initialize, 0, packet, 0, initialize.length);
        System.arraycopy(cmd, 0, packet, initialize.length, cmd.length);
        System.arraycopy(dataLength, 0, packet, initialize.length + cmd.length, dataLength.length);
        System.arraycopy(separator, 0, packet, initialize.length + cmd.length + dataLength.length,
                separator.length);
        System.arraycopy(data, 0, packet,
                initialize.length + cmd.length + dataLength.length + separator.length,
                data.length);

        return packet;
    }

    public static List<String> getFileList(String dirName) throws IOException {
        return Files.walk(Path.of(dirName))
                .filter(Files::isRegularFile)
                .map(Path::getFileName)
                .map(Path::toString)
                .collect(Collectors.toList());
    }

    private static int getNumber(String str){
        Map<String, Integer> numberMap = new HashMap<>();
        numberMap.put("read", 4);

        return numberMap.get(str);
    }
}
