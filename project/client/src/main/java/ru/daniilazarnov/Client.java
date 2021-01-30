package ru.daniilazarnov;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

public class Client {

    /*
            [byte  ] 1b управляющий байт →
            [short ] 2b длинна имени файла →
            [byte[]] nb  имя файла →
            [long  ] 8b размер файла →
            [byte[]] nb содержимое файла
     */
    private static final Logger log = Logger.getLogger(Client.class);
    private static Selector selector;
    private static SocketChannel client;


    public static void main(String[] args) throws IOException {
        client = SocketChannel.open();
        client.configureBlocking(false);
        selector = Selector.open();
        client.register(selector, SelectionKey.OP_READ);

        String msg = "GHello\n";
        String hostname = "localhost";
        int port = 8189;
        connect(hostname, port);


        readFile();
        ByteBuffer buffer;
        buffer = sendMessage(client, msg);
        readingInMessage(client, buffer);

        Iterator<SelectionKey> iter;
        SelectionKey key;

        while (client.isOpen()) { // внутри вайла канал открыт
            selector.select();// ждем новых событий
            iter = selector.selectedKeys().iterator();
            while (iter.hasNext()) { // перебираем ключи в итераторе событий
                key = iter.next(); //получаем ссылку на событие
                iter.remove(); //удаляем событие из списка обработки
                if (key.isAcceptable()) { // проверяем ключ
//                    handleAccept(key); // если подключился
                }
                if (key.isReadable()) { //если что-то написал
//                    handleRead(key);
                }
            }
        }
    }

    private static void readFile() throws IOException {
        ByteBuffer prefixBuf = ByteBuffer.wrap("F".getBytes());
        RandomAccessFile randomAccessFile = new RandomAccessFile("data/nio-data.txt", "rw");
        FileChannel inChannel = randomAccessFile.getChannel();
        ByteBuffer buf = ByteBuffer.allocate(48);
        int bytesRead = inChannel.read(buf);
        while (bytesRead != -1) {
            buf.flip();
            int count = 0;
            while (buf.hasRemaining()) {
                if (count == 0) {
                    client.write(prefixBuf);
                }
//                System.out.print((char) buf.get());
                client.write(buf);
                count++;
            }
            buf.clear();
            bytesRead = inChannel.read(buf);
        }
        randomAccessFile.close();

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
        buffer.clear(); //очищаем буфер
        int read;
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
        System.out.println(sb.toString());
        log.debug("Out of the loop");
    }
}
