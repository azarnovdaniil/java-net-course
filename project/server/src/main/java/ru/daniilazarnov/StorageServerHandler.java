package ru.daniilazarnov;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ru.daniilazarnov.data.CommandData;
import ru.daniilazarnov.data.CommonData;
import ru.daniilazarnov.data.FileData;
import ru.daniilazarnov.data.TypeMessages;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.Iterator;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StorageServerHandler implements Runnable {
    private static final Logger logger = Logger.getLogger(StorageServerHandler.class.getName());
    public static final int SIZE_META = 10;

    private StorageServer storageServer;
    private Selector selector;
    private final ByteBuffer buffer = ByteBuffer.allocate(48);



    public StorageServerHandler (StorageServer storageServer) {
        this.storageServer = storageServer;
    }

    @Override
    public void run() {
        ServerSocketChannel serverSocketChannel = this.storageServer.getServerSocketChannel();
        this.selector = this.storageServer.getSelector();
        try {

            Iterator<SelectionKey> iter;
            SelectionKey key;

            while (serverSocketChannel.isOpen()) {
                selector.select();
                iter = selector.selectedKeys().iterator();
                while (iter.hasNext()) {
                    key = iter.next();
                    iter.remove();
                    if (key.isAcceptable()) {
                        this.handleAccept(key, selector);
                    }
                    if (key.isReadable()) {
                        handleRead(key);
                    }
                }
            }

        } catch (Exception e) {
            logger.log(Level.WARNING, "StorageServerHandler: IOException ");
            e.printStackTrace();
        }
    }


    private void handleAccept(SelectionKey key, Selector selector) throws IOException {
        ConnectedHandler connectedHandler = new ConnectedHandler(this);

        SocketChannel sc = ((ServerSocketChannel) key.channel()).accept();
        sc.configureBlocking(false);
        sc.register(selector, SelectionKey.OP_READ, connectedHandler);
        logger.log(Level.INFO, "Подключился новый Socket ID "+ connectedHandler.getId());

        CommandData welcome = new CommandData();
        welcome.addEcho("Welcome to Storage Server, please auth! Show HELP -h");
        broadcastMessage(welcome);
    }



    private void handleRead(SelectionKey key) throws IOException {
        ConnectedHandler cHandler = (ConnectedHandler) key.attachment();
        SocketChannel ch = (SocketChannel) key.channel();

        cHandler.setSocketChannel(ch);
        cHandler.createTempFile();

        buffer.clear();

        int read = 0;
        while ((read = ch.read(buffer)) > 0) {
            byte[] bytes = new byte[buffer.limit()];
            buffer.flip();
            buffer.get(bytes);

            try (FileOutputStream fos = new FileOutputStream(cHandler.getTempFile(), true)) {
                fos.write(bytes);
            }

            buffer.clear();
        }

        FileInputStream fRead = new FileInputStream(cHandler.getTempFile());

        byte[] sizeMeteByte = new byte[SIZE_META];
        fRead.read(sizeMeteByte, 0, SIZE_META);
        int sizeMeteData = Integer.parseInt(new String(sizeMeteByte));
        byte[] sizeMeteDataByte = new byte[sizeMeteData];
        fRead.read(sizeMeteDataByte, 0, sizeMeteData);
        fRead.close();

        cHandler.addCommand(sizeMeteDataByte);
        cHandler.removeTempFile();

        if (read < 0) {
            ch.close();
        }
    }



//    private void handleRead(SelectionKey key) throws IOException {
//        ConnectedHandler cHandler = (ConnectedHandler) key.attachment();
//        SocketChannel ch = (SocketChannel) key.channel();
//        cHandler.setSocketChannel(ch);
//
//
//        buffer.clear();
//        int read = 0;
//        long totalRead = 0;
//        while ((read = ch.read(buffer)) > 0) {
//            int sizeMetaBuffer = cHandler.metaData.toString().getBytes().length;
//            byte[] bytes = new byte[buffer.limit()];
//
//            buffer.flip();
//            buffer.get(bytes);
//
//            if (cHandler.sizeMetaData == -1 && totalRead == 0) {
//                cHandler.sizeMetaData = Integer.parseInt(new String(Arrays.copyOf(bytes, 10)));
//                if (cHandler.sizeMetaData < bytes.length) {
//                    if ((cHandler.sizeMetaData + 10) < bytes.length) {
//                        cHandler.metaData.append(new String(bytes).substring(10, cHandler.sizeMetaData + 10));
//                        bytes = Arrays.copyOfRange(bytes, cHandler.sizeMetaData + 10, bytes.length);
//                    } else {
//                        cHandler.metaData.append(new String(bytes).substring(10, bytes.length));
//                    }
//                } else {
//                    cHandler.metaData.append(new String(bytes).substring(10, bytes.length));
//                }
//            } else if (sizeMetaBuffer < cHandler.sizeMetaData) {
//                int сheckSize = sizeMetaBuffer + bytes.length;
//                if (сheckSize > cHandler.sizeMetaData) {
//                    int difference = сheckSize - cHandler.sizeMetaData;
//                    cHandler.metaData.append(new String(Arrays.copyOf(bytes, bytes.length - difference)));
//                    bytes = Arrays.copyOfRange(bytes, bytes.length - difference, bytes.length);
//                } else {
//                    cHandler.metaData.append(new String(bytes));
//                }
//            }
//
//            if (cHandler.metaData.toString().getBytes().length == cHandler.sizeMetaData) {
//                JsonObject jsonObject = JsonParser.parseString(cHandler.metaData.toString()).getAsJsonObject();
//                cHandler.addCommand(jsonObject);
//            }
//
//            if (cHandler.getCountCommand() > 0 && cHandler.getCommand().getType() == TypeMessages.FILE) {
//                String path = StorageUtil.getFilePath((FileData) cHandler.getCommand());
//                long pathSize = StorageUtil.getSizeFilePath(path);
//                long fileSize = ((FileData) cHandler.getCommand()).getLengthByte();
//
//                if ((pathSize+bytes.length) > fileSize) {
//                    int difference = (int) ((pathSize + bytes.length) - fileSize);
//                    bytes = Arrays.copyOf(bytes, bytes.length - difference);
//                }
//
//                try (FileOutputStream fos = new FileOutputStream(path, true)) {
//                    fos.write(bytes);
//                }
//            }
//
//            totalRead = totalRead + read;
//            buffer.clear();
//        }
//
//        logger.log(Level.INFO, "HandleRead while end ID "+ cHandler.getId());
//
//        if (read < 0) {
//            ch.close();
//        }
//
//    }

    public <T extends CommonData> void  handleWrite (T objectData, SocketChannel channel) {
        byte[] dataJson = objectData.jsonToString().getBytes();

        byte[] size = String.format("%010d", dataJson.length).getBytes();
        byte[] dataBytes = this.unionByteArray(size, dataJson);

        int restRead = 0;
        byte[] data = new byte[buffer.limit()];
        try ( InputStream is = new ByteArrayInputStream(dataBytes) ){
            int nRead = 0;
            while ((nRead = is.read(data, 0, data.length)) != -1) {
                if ((data.length - nRead) == 0) {
                    ByteBuffer buffer = ByteBuffer.wrap(data);
                    channel.write(buffer);
                } else {
                    restRead = nRead;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (objectData.getType() != TypeMessages.FILE) {
            try {
                ByteBuffer buffer = ByteBuffer.wrap(data);
                channel.write(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return;
        }

        File fromFile = new File(((FileData) objectData).getFromPath());
        try (FileInputStream is = new FileInputStream(fromFile)) {
            if ((data.length - restRead) != 0) {
                byte[] restReadByte = new byte[data.length - restRead];
                is.read(restReadByte, 0, restReadByte.length);
                byte[] oldBytes = this.replaceByteArray(data, restReadByte);
                ByteBuffer buffer = ByteBuffer.wrap(oldBytes);
                channel.write(buffer);
            }

            int nRead = 0;
            while ((nRead = is.read(data, 0, data.length)) != -1) {
                ByteBuffer buffer = ByteBuffer.wrap(data);
                channel.write(buffer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private <T extends CommonData> void broadcastMessage(T objectData) throws IOException {
        for (SelectionKey key : this.storageServer.getSelector().keys()) {
            if (key.isValid() && key.channel() instanceof SocketChannel) {
                SocketChannel sch = (SocketChannel) key.channel();
                this.handleWrite(objectData, sch);
            }
        }
    }

    public byte[] unionByteArray (byte[] firstArray, byte[] secondArray) {
        byte[] dataBytes = Arrays.copyOf(firstArray, secondArray.length + firstArray.length);
        System.arraycopy(secondArray, 0, dataBytes, firstArray.length, secondArray.length);
        return dataBytes;
    }

    public byte[] replaceByteArray (byte[] firstArray, byte[] secondArray) {
        System.arraycopy(secondArray, 0, firstArray, firstArray.length - secondArray.length , secondArray.length);
        return firstArray;
    }
}
