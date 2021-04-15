package ru.kgogolev;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.StandardCharsets;

public final class StringUtil {
    private StringUtil() {
    }

    public static String getWordFromLine(String line, int number) {
        String[] lineToArray = line.split(" ");
        return lineToArray.length == 1 ? "" : lineToArray[number];
    }

    public static boolean startsWith(String line, String prefix) {
        return line.startsWith(prefix);
    }

    public static ByteBuf lineToByteBuf(String line) {
        return Unpooled.wrappedBuffer(line.getBytes(StandardCharsets.UTF_8));
    }
}
