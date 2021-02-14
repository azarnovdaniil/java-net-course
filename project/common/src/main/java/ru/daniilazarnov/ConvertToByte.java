package ru.daniilazarnov;

import java.io.*;

public class ConvertToByte {

    public static byte[] serialize(Object obj) {
        try (ByteArrayOutputStream b = new ByteArrayOutputStream()) {
            try (ObjectOutputStream o = new ObjectOutputStream(b)) {
                o.writeObject(obj);
            }
            return b.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static Object deserialize(byte[] bytes) {
        try (ByteArrayInputStream b = new ByteArrayInputStream(bytes)) {
            try (ObjectInputStream o = new ObjectInputStream(b)) {
                return o.readObject();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
