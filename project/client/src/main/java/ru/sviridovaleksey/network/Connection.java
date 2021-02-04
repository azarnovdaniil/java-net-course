package ru.sviridovaleksey.network;

import org.apache.commons.lang3.SerializationUtils;
import ru.sviridovaleksey.Command;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Connection {

    private String serverAddress;
    private int usePort;
    private SocketChannel SocketChannel;
    private Selector selector;
    private int bufCapacity = 1024;
    private Scanner scanner = new Scanner(System.in);
    private ByteBuffer bufRead = ByteBuffer.allocate(bufCapacity);
    private ByteBuffer bufWrite = ByteBuffer.allocate(bufCapacity);
    private WhatDoClient whatDoClient = new WhatDoClient();


    public Connection(String serverAddress, int usePort) {
        this.serverAddress = serverAddress;
        this.usePort = usePort;

    }

    public void openConnection() {
        HelloMessage helloMessage = new HelloMessage();
        try {
            SocketChannel = SocketChannel.open();
            SocketChannel.connect(new InetSocketAddress(serverAddress, usePort));
            selector = Selector.open();
            System.out.println("Соединение установлено");
            helloMessage.helloMessage();
            ChoseAction choseAction = new ChoseAction(this, scanner);
            while (SocketChannel.isOpen()) {
                System.out.println("введите код действия:");
                String chose = scanner.next();
                choseAction.choseAction(chose);
            }

        } catch (IOException e) {
            System.out.println("Соединение не установленно или разорванно " + e.getMessage());
        }
    }

    public void writeMessage (Command command)  {

        bufWrite.clear();
        byte[] by =SerializationUtils.serialize(command);
        bufWrite.put(by);
        bufWrite.flip();
        while (bufWrite.hasRemaining()) {
            try {
                SocketChannel.write(bufWrite);
            } catch (IOException e) {
                System.out.println("Не удачная отправка на сервер "  + e.getMessage());
                e.printStackTrace();
            }
        }
        bufWrite.clear();

    }


}
