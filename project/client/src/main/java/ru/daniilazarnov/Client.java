package ru.daniilazarnov;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.AlreadyConnectedException;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.sql.Timestamp;
import java.util.Date;

public class Client {

    /*
            [byte  ] 1b управляющий байт →
            [short ] 2b длинна имени файла →
            [byte[]] nb  имя файла →
            [long  ] 8b размер файла →
            [byte[]] nb содержимое файла
     */
    private static final Logger log = Logger.getLogger(Client.class);


    //    private static SocketChannel client;
    private final Selector selector;
    private static SocketChannel client;

    public Client(Selector selector) {
        this.selector = selector;
    }

    public static void main(String[] args) throws IOException {
        client = SocketChannel.open();
        String msg = "Hello\n";
        String hostname = "localhost";
        int port = 8189;
        connect(hostname, port);
        ByteBuffer buffer;
        buffer = sendMessage(client, msg);
        readingInMessage(client, buffer);

//        while (client.isOpen()) {
//            long before = System.currentTimeMillis();
//            long after = 0L;
//            if (before - after >= 1000) {
//                log.info("client.isOpen()");
//            }
//            after = System.currentTimeMillis();
//
//        }
    }

    private static void connect(String hostname, int port) throws IOException {
        InetSocketAddress serverAddress = new InetSocketAddress(hostname,
                port);


        /*В этом цикле ждем запуск сервера*/
        while (!client.isConnected()) {
            client = SocketChannel.open();
            try {
                client.connect(serverAddress);
            } catch (ConnectException | AsynchronousCloseException e) {
                log.info("Ожидание сервера");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException interruptedException) {
                    log.error(interruptedException);
                }
            }

        }

//        client.connect(serverAddress);
//        log.debug();
        log.info("Соединение с сервером установлено");
    }

    //    @org.jetbrains.annotations.NotNull
    private static ByteBuffer sendMessage(SocketChannel client, String msg) throws IOException {
        ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
//        log.debug(buffer);
        client.write(buffer);
        return buffer;
    }

    private static void readingInMessage(SocketChannel client, ByteBuffer buffer) throws IOException {
        SocketChannel ch = client.socket().getChannel();
        StringBuilder sb = new StringBuilder();
//        int read;
        buffer.clear(); //очищаем буфер
        int read;
//        while (read != -1) {

        while ((read = ch.read(buffer)) != -1) { //читаем из канала
            int positions = buffer.position();
            buffer.flip();
            byte[] bytes = new byte[buffer.limit()];
            buffer.get(bytes); //записываем в данные из буфера в массив
            sb.append(new String(bytes)); // добавляем в стринг билдер
            buffer.clear();
            if (positions < 6) {
                break;
            }
        }
        log.debug("sb: " + sb.toString());
        if (read < 0) {
            ch.close();
        }


        log.debug("Out of the loop");
    }
}
