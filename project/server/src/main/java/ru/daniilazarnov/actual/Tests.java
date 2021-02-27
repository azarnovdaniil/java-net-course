package ru.daniilazarnov.actual;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Tests {
    public static void main(String[] args) throws Exception {
//        File file = new File("d:\\testDir\\Client\\ClientNetwork.zip");
//        Path path = Path.of(String.valueOf(file));
//        byte [] prefix = {1, 2, 3};
//        fileToBBWithPrefix(path, prefix);
        String f1 = "d:\\testDir\\Client\\ClientNetwork.zip";
        String f2 = "d:\\testDir\\Server\\ClientNetwork.zip";
//        copyFile(f1, f2);
//        copyFileWithFileChannel(f1,f2);
//        ls();
        listToBytesAndBytesToList();
    }

    static void ls(){
        File dir = new File("d:\\testDir\\Server\\admin\\");
        File[] files = dir.listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                if (!file.isDirectory()) {
                    System.out.println(String.format("[%s] size: [%s]", file.getName(), Utils.bytesConverter(file.length())));
                }
            }
        }
    }

    static void listToBytesAndBytesToList() throws IOException, ClassNotFoundException {
        List<String> listOut = new ArrayList<>();
        listOut.add("one");
        listOut.add("two");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(listOut);
        byte[] bytes = bos.toByteArray();
        oos.close();
        bos.close();

        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bis);
        Object ob = ois.readObject();
        List list = new ArrayList<>();
        if (ob instanceof List){
            list = (List) ob;
        }
        for (Object s : list){
            System.out.println(s);
        }
        bis.close();
        ois.close();
    }

    static void fileToBBWithPrefix(Path path, byte[] prefix) throws IOException {
        byte[] signal = prefix;
        byte[] fileName = path.getFileName().toString().getBytes();
        byte[] content = Files.readAllBytes(path);

        ByteBuf bufSignal = ByteBufAllocator.DEFAULT.directBuffer();
        bufSignal.writeBytes(signal);

        ByteBuf bufName = ByteBufAllocator.DEFAULT.directBuffer();
        bufName.writeBytes(fileName);

        ByteBuf bufContent = ByteBufAllocator.DEFAULT.directBuffer();
        bufContent.writeBytes(content);

        CompositeByteBuf comp = Unpooled.compositeBuffer();
        comp.addComponents(bufSignal, bufName, bufContent);

        convertBufToFile(comp);
    }

    static void convertBufToFile (CompositeByteBuf comp) throws IOException {
        byte [] prefix = {1, 2, 3};
        byte[] check = new byte[3];
        int idx = 0;

        ByteBuf bufSignal = comp.component(0);
        ByteBuf bufName = comp.component(1);
        ByteBuf bufContent = comp.component(2);

        while (bufSignal.readableBytes() > 0){
            check[idx] = bufSignal.readByte();
            idx++;
            if (idx == 3){
                break;
            }
        }

        if (Arrays.equals(prefix, check)){
            File file = new File("d:\\testDir\\Server\\" + bufName.toString(StandardCharsets.UTF_8));
            if (file.createNewFile()){
                FileOutputStream fos = new FileOutputStream(file);
                byte[] bytes = new byte[bufContent.readableBytes()];
                bufContent.readBytes(bytes);
                fos.write(bytes);
                System.out.println("File saved");
            }
        } else {
            System.out.println("fail");
        }
    }

    public static void copyFile(String srcFile, String destFile) throws Exception {
        byte[] temp = new byte[1024];
        FileInputStream in = new FileInputStream(srcFile);
        FileOutputStream out = new FileOutputStream(destFile);
        int length;
        while ((length = in.read(temp)) != -1) {
            out.write(temp, 0, length);
        }

        in.close();
        out.close();
    }

    public static void copyFileWithFileChannel(String srcFileName, String destFileName) throws Exception {
        RandomAccessFile srcFile = new RandomAccessFile(srcFileName, "r");
        FileChannel srcFileChannel = srcFile.getChannel();

        RandomAccessFile destFile = new RandomAccessFile(destFileName, "rw");
        FileChannel destFileChannel = destFile.getChannel();

        long position = 0;
        long count = srcFileChannel.size();

        srcFileChannel.transferTo(position, count, destFileChannel);
    }
}
