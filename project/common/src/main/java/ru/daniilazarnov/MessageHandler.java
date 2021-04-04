package ru.daniilazarnov;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;

public class MessageHandler {
    public byte[] message;
    public int code;
    public String[] arguments;

    public MessageHandler(String user_input) {
        HashMap<String, Integer> commandMap = new HashMap<String, Integer>() {{
            put("-exit", 255);
            put("-help", -2);
            put("-upload", 1);
            put("-download", 2);
            put("-show", 3);
            put("-remove", 4);
            put("-rename", 5);
        }};
        String[] parsed = user_input.trim().split(" ");
        try {
            this.code = commandMap.get(parsed[0]);
            this.arguments = Arrays.copyOfRange(parsed, 1, parsed.length);
        } catch (NullPointerException e) {
            this.code = -1;
            System.out.printf("!- Command %s is not supported.\n", parsed[0]);
        }

        switch (this.code) {
            case -2:
                System.out.println("The following commands are supported:");
                System.out.println("-exit       Disconnect and exit.");
                System.out.println("-help       Show help.");
                System.out.println("-upload     Upload a file.");
                System.out.println("-download   Download a file.");
                System.out.println("-show       Show files.");
                System.out.println("-remove     Remove file.");
                System.out.println("-rename     Rename file.");
                break;
            case 1:
                this.message = uploadFile();
                break;
            case 2:
                this.message = downloadFile();
                break;
            case 3:
                this.message = showFiles();
                break;
            case 4:
                this.message = removeFile();
                break;
            case 5:
                this.message = renameFile();
                break;
            case 255:
                this.message = exit();
                break;
        }
    }

    private byte[] renameFile() {
        byte[] codeBytes = intToBytes(this.code);
        File oldFile = new File(this.arguments[0]);
        File newFile = new File(this.arguments[1]);
        String header = oldFile.getName() + ";" + newFile.getName();
        byte[] headerBytes = header.getBytes();
        return composeMessage(codeBytes, headerBytes);
    }

    private byte[] removeFile() {
        byte[] codeBytes = intToBytes(this.code);
        File file = new File(this.arguments[0]);
        String header = file.getName();
        byte[] headerBytes = header.getBytes();
        return composeMessage(codeBytes, headerBytes);
    }

    private byte[] showFiles() {
        byte[] codeBytes = intToBytes(this.code);
        return composeMessage(codeBytes);
    }

    private byte[] exit() {
        byte[] codeBytes = intToBytes(this.code);
        return composeMessage(codeBytes);
    }

    private byte[] downloadFile() {
        byte[] codeBytes = intToBytes(this.code);
        File file = new File(this.arguments[0]);
        String header = file.getName();
        byte[] headerBytes = header.getBytes();
        return composeMessage(codeBytes, headerBytes);
    }

    public byte[] uploadFile() {

        try {
            File file = new File(this.arguments[0]);
            String header = file.getName() + ";";
            byte[] headerBytes = header.getBytes();
            byte[] fileBytes = new byte[(int) file.length()];

            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            bis.read(fileBytes, 0, fileBytes.length);

            return composeMessage(headerBytes, fileBytes);

    } catch (FileNotFoundException e) {
            System.out.println("!- File not found.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    private byte[] intToBytes( final int i ) {
        ByteBuffer bb = ByteBuffer.allocate(4);
        bb.putInt(i);
        return bb.array();
    }

    private byte[] composeMessage(byte[]... args) {

        int length = 0;
        for (byte [] arg: args) {
            length += arg.length;
        }

        byte[] lengthBytes = intToBytes(length);
        byte[] codeBytes = intToBytes(this.code);
        byte[] messageBytes = new byte[lengthBytes.length + codeBytes.length + length];

        int destPos = 0;
        System.arraycopy(lengthBytes, 0, messageBytes, destPos, lengthBytes.length);
        destPos += lengthBytes.length;
        System.arraycopy(codeBytes, 0, messageBytes, destPos, codeBytes.length);
        destPos += codeBytes.length;

        for (byte[] arg : args) {
            System.arraycopy(arg, 0, messageBytes, destPos, arg.length);
            destPos += arg.length;
        }

        return messageBytes;
    }
}
