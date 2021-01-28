package ru.daniilazarnov.data;

import java.io.*;

public class CommonData implements iData, Serializable {

    private TypeMessages type;

    public CommonData (TypeMessages type) {
        this.type = type;
    }

    @Override
    public TypeMessages getType() {
        return this.type;
    }

    public static byte[] serialize(Serializable o) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try(ObjectOutputStream outputStream = new ObjectOutputStream(out)) {
            outputStream.writeObject(o);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return out.toByteArray();
    }

    public static <T extends Serializable> T deserialize(byte[] data) throws IOException, ClassNotFoundException {
        try(ByteArrayInputStream bis = new ByteArrayInputStream(data)) {
            return (T) new ObjectInputStream(bis).readObject();
        }
    }
}
