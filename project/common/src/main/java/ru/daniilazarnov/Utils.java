package ru.daniilazarnov;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.StandardCharsets;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

public final class Utils {

    /** Преобразует полученную строку в ByteBuf */

    public static ByteBuf stringToByteBuf(String string){
        ByteBuf buf;
        byte[] bytes = string.getBytes(StandardCharsets.UTF_8);
        buf = Unpooled.wrappedBuffer(bytes);
        return buf;
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
}
