package ru.daniilazarnov.test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Utils {

    /** Преобразует полученную строку в ByteBuf */

    public static ByteBuf convertToByteBuf(String string){
        ByteBuf buf;
        byte[] bytes = string.getBytes(StandardCharsets.UTF_8);
        buf = Unpooled.wrappedBuffer(bytes);
        return buf;
    }

    public static ByteBuf convertToByteBuf(byte[] bytes){
        return Unpooled.wrappedBuffer(bytes);
    }

    /** Преобразует полученные байты в читабельный вид */

    public static String bytesConverter(long bytes) {
        if (-1000 < bytes && bytes < 1000) {
            return bytes + " B";
        }
        CharacterIterator ci = new StringCharacterIterator("kMGTPE");
        while (bytes <= -999_950 || bytes >= 999_950) {
            bytes /= 1000;
            ci.next();
        }
        return String.format("%.1f %cB", bytes / 1000.0, ci.current());
    }

    /** Проверяет совпадает ли префикс (первые три байта) ByteBuf и контрольный сигнал */

    public static boolean prefixCheck(ByteBuf buf, byte[] signal){
        byte[] check = new byte[3];
        int idx = 0;
        while (buf.readableBytes() > 0){
            check[idx] = buf.readByte();
            idx++;
            if (idx == 3){
                break;
            }
        }
        return Arrays.equals(signal, check);
    }

    /** Добавляет префикс к ByteBuf или byte[] и возвращает полученный ByteBuf */

    public static ByteBuf addPrefixToByteBuf(ByteBuf buf, byte[] prefix){
        ByteBuf resultBuf = ByteBufAllocator.DEFAULT.directBuffer();
        resultBuf.writeBytes(prefix);
        resultBuf.writeBytes(buf);
        return resultBuf;
    }

    public static ByteBuf addPrefixToByteBuf(byte[] bytes, byte[] prefix){
        ByteBuf buf = ByteBufAllocator.DEFAULT.directBuffer();
        buf.writeBytes(prefix);
        buf.writeBytes(bytes);
        return buf;
    }

    /**
     * Возвращает ByteBuf из переданного List
     * @param list {@link List}
     * @return {@link ByteBuf}
     */

    public static ByteBuf convertListToByteBuf(List list) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(list);
        byte[] bytes = bos.toByteArray();
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
        buf.writeBytes(Utils.convertToByteBuf(bytes));
        bos.close();
        oos.close();
        return buf;
    }

    /**
     * Возвращает List из переданного ByteBuf
     * @param buf {@link ByteBuf}
     * @return {@link List}
     */

    public static List convertByteBufToList (ByteBuf buf) throws IOException, ClassNotFoundException {
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);

        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bis);
        Object ob = ois.readObject();
        List list = new ArrayList<>();
        if (ob instanceof List){
            list = (List) ob;
        }
        bis.close();
        ois.close();
        return list;
    }
}
