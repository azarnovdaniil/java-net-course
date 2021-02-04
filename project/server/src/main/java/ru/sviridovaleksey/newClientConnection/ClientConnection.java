package ru.sviridovaleksey.newClientConnection;

import org.apache.commons.lang3.SerializationUtils;
import ru.sviridovaleksey.Command;
import ru.sviridovaleksey.commands.AuthCommandData;
import ru.sviridovaleksey.commands.CreateNewDirectory;
import ru.sviridovaleksey.commands.CreateNewFile;
import ru.sviridovaleksey.commands.MessageCommandData;
import ru.sviridovaleksey.workwithfiles.WorkWithFile;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Handler;
import java.util.logging.Logger;


public class ClientConnection {

    private static final Logger logger = Logger.getLogger(ClientConnection.class.getName());
    private final ServerSocketChannel serverSocketChannel;
    private final Selector selector;
    private int acceptedClientIndex = 1;
    private final ByteBuffer welcomeBuf = ByteBuffer.wrap("Добро пожаловать в чат!\n".getBytes());
    private SocketChannel clientSocket;
    private static WorkWithFile workWithFile = new WorkWithFile();
    private WhatDo whatDo = new WhatDo(workWithFile);
    private int bufCapacity = 1024;
    private ByteBuffer bufRead = ByteBuffer.allocate(bufCapacity);
    private ByteBuffer bufWrite = ByteBuffer.allocate(bufCapacity);


    public ClientConnection(int usePort, Handler fileHandler) throws IOException {
        this.logger.addHandler(fileHandler);
        this.logger.setUseParentHandlers(false);
        this.serverSocketChannel = ServerSocketChannel.open();
        this.serverSocketChannel.socket().bind(new InetSocketAddress(usePort));
        this.serverSocketChannel.configureBlocking(false);
        this.selector = Selector.open();
        this.serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);


    }

    public void startConnection() throws IOException { //

        System.out.println("Сервер запущен");

        while (this.serverSocketChannel.isOpen()) {

            waitNewClientConnection();


        }


    }

    private void waitNewClientConnection() throws IOException { //

        Iterator<SelectionKey> iter;
        SelectionKey key;
        try {
            selector.select();


            iter = selector.selectedKeys().iterator();
            while (iter.hasNext()) {
                key = iter.next();
                iter.remove();
                if (key.isAcceptable()) {
                    handleAccept(key);
                }

                if (key.isReadable()) {
                    handleRead(key);
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage() + " Связь потеряна");
            selector.close();
        }

    }

    private void handleAccept(SelectionKey key) throws IOException {
        SocketChannel sc = ((ServerSocketChannel) key.channel()).accept();
        String clientName = "Клиент #" + acceptedClientIndex;
        acceptedClientIndex++;
        sc.configureBlocking(false);
        sc.register(selector, SelectionKey.OP_READ, clientName);
        sc.write(welcomeBuf);
        welcomeBuf.rewind();
        System.out.println("Подключился новый клиент " + clientName);
        workWithFile.createFirsDirectory("user1");

    }


    private void handleRead(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        bufRead.clear();
        int read = 0;
        while ((read = channel.read(bufRead)) > 0) {
            if (channel.read(bufRead) < 0) {
                continue;
            }
                bufRead.flip();
                byte[] bytes = new byte[bufRead.limit()];
                bufRead.get(bytes);
                Command command = SerializationUtils.deserialize(bytes);
                    whatDo.whatDo(command, key);
                    bufRead.clear();

        }

    }


}






