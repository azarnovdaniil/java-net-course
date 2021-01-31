package ru.daniilazarnov;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;

public class Client {

    /*
    Нереализованный протокол:
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
        initConnection();

        String msg = "Hello\nHello\nHello\n";
        ByteBuffer prefixBuf = ByteBuffer.wrap("G".getBytes()); // добавляем команду управления в первый байт
        byte lengthFileByte = (byte) msg.length(); //получаем размер файла
        String hostname = "localhost";
        int port = 8189;
        connect(hostname, port);

        readFile();
        sendMessage(client, msg, prefixBuf, lengthFileByte);
        readingInMessage(client);
    }

    /**
     * Инициализация сетевого соединения
     *
     * @throws IOException исключение
     */
    private static void initConnection() throws IOException {
        client = SocketChannel.open();
        client.configureBlocking(false);
        selector = Selector.open();
        client.register(selector, SelectionKey.OP_READ);
    }

    /**
     * Читает файл по переданному пути // TODO: 31.01.2021 функционал реализован не полностью
     * и отправляет напрямую в канал
     *
     * @throws IOException исключение
     */
    private static void readFile() throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile("data/nio-data.txt", "rw");
        ByteBuffer prefixBuf = ByteBuffer.wrap("F".getBytes()); // добавляем команду управления в первый байт
        byte lengthFileByte = (byte) randomAccessFile.length(); // добавляем длинну файла во второй байт
        ByteBuffer lengthFile = ByteBuffer.wrap(new byte[]{lengthFileByte});
        FileChannel inChannel = randomAccessFile.getChannel();
        ByteBuffer buf = ByteBuffer.allocate(48);

        int bytesRead = inChannel.read(buf);
        while (bytesRead != -1) {
            buf.flip();
            int count = 0;
            while (buf.hasRemaining()) {
                if (count == 0) {
                    client.write(prefixBuf);
                    client.write(lengthFile);
                }
                client.write(buf);
                count++;
            }
            buf.clear();
            bytesRead = inChannel.read(buf);
        }
        randomAccessFile.close();
    }

    /**
     * Создает соединение
     *
     * @param hostname ip адресс сервера
     * @param port     порт
     * @throws IOException исключение
     */
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
        log.info("Соединение с сервером установлено");
    }

    /**
     * Отправляет сообщение, возвращает зачем то буфер // TODO: 31.01.2021 разобраться зачем возвращает буфер
     *
     * @param client         открытый сокет
     * @param msg            сообщение для отправки
     * @param prefixBuf      управлюящий байт
     * @param lengthFileByte байт содержащий информацию о длинне передаваемых данных
     * @return буфер
     * @throws IOException исключение
     */
    private static ByteBuffer sendMessage(SocketChannel client, String msg, ByteBuffer prefixBuf, byte lengthFileByte) throws IOException {
        ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
        ByteBuffer lengthFile = ByteBuffer.wrap(new byte[]{lengthFileByte});
        client.write(prefixBuf);
        client.write(lengthFile);
        client.write(buffer);
        return buffer;
    }

    /**
     * Читает сообщение
     *
     * @param client открытый сокет
     * @throws IOException исключение
     */
    private static void readingInMessage(SocketChannel client) throws IOException {
        int bufferSize = 10;
        ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
        SocketChannel ch = client.socket().getChannel();
        StringBuilder sb = new StringBuilder();
        buffer.clear(); //очищаем буфер
        int read;
        while (true) {
            while ((read = ch.read(buffer)) != -1) { //читаем из канала
                int positions = buffer.position();
                buffer.flip();
                byte[] bytes = new byte[buffer.limit()];
                buffer.get(bytes); //записываем в данные из буфера в массив

                sb.append(new String(bytes)); // добавляем в стринг билдер
                buffer.clear();
                if (positions < bufferSize) {
                    break;
                }
            }
            System.out.printf("%s\n", sb.toString());
            sb.setLength(0);
        }
    }
}
