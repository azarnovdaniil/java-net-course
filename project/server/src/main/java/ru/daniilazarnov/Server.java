package ru.daniilazarnov;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

public class Server implements Runnable {
    private static final Logger log = Logger.getLogger(Server.class);
    private final ServerSocketChannel serverSocketChannel;
    private final Selector selector; // слушает все события
    private final ByteBuffer buf = ByteBuffer.allocate(256);
    private int acceptedClientIndex = 1;
    private final ByteBuffer welcomeBuf = ByteBuffer.wrap("Welcome to chat!\n".getBytes());

    Server() throws IOException {
        this.serverSocketChannel = ServerSocketChannel.open(); //создаем ССЧ
        this.serverSocketChannel.socket().bind(new InetSocketAddress(8189)); //устанавливаем порт для ССЧ, который он будет слушать
        this.serverSocketChannel.configureBlocking(false);
        this.selector = Selector.open();
        this.serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT); // регистрация селектора на канналше с определенным ключом
    }

    public static void main(String[] args) throws IOException {
        new Thread(new Server()).start();
    }

    @Override
    public void run() {
        try {
            log.info("Сервер запущен (Порт: 8189)");
            Iterator<SelectionKey> iter;
            SelectionKey key;
            while (this.serverSocketChannel.isOpen()) { // внутри вайла канал открыт
                selector.select();// ждем новых ключей
                iter = this.selector.selectedKeys().iterator();
                while (iter.hasNext()) { // перебираем ключи в итераторе
                    key = iter.next();
                    iter.remove();
                    if (key.isAcceptable()) { // проверяем ключ
                        handleAccept(key);
                    }
                    if (key.isReadable()) {
                        handleRead(key);
                    }
                }
            }
        } catch (IOException e) {
//            e.printStackTrace();
            log.error(e.getLocalizedMessage());
        }
    }

    /**
     * Метод подключает новых клиентов
     * @param key Ключ выбора создается каждый раз, когда канал регистрируется с помощью селектора.
     *            Ключ остается действительным до тех пор, пока он не будет отменен вызовом его метода cancel
     * @throws IOException исключение
     */
    private void handleAccept(SelectionKey key) throws IOException {
        SocketChannel sc = ((ServerSocketChannel) key.channel()).accept(); // получаем канал из ключа
        String clientName = "client #" + acceptedClientIndex;
        acceptedClientIndex++;
        sc.configureBlocking(false);
        sc.register(selector, SelectionKey.OP_READ, clientName);// регистрируем ключ на чтение и предаем вместе с именем клиента
        sc.write(welcomeBuf);
        welcomeBuf.rewind();
        log.info("Подключился новый клиент " + clientName);
    }


    /**
     * Чтение из канала
     *
     * @param key ключ
     * @throws IOException исключени
     */
    private void handleRead(SelectionKey key) throws IOException {
        SocketChannel ch = (SocketChannel) key.channel();
        StringBuilder sb = new StringBuilder();

        buf.clear(); //очищаем буфер
        int read;
        while ((read = ch.read(buf)) > 0) { //читаем из канала
            buf.flip();
            byte[] bytes = new byte[buf.limit()];
            buf.get(bytes); //записываем в буфер
            sb.append(new String(bytes));
            buf.clear();
        }
        String msg;
        if (read < 0) {
            msg = key.attachment() + " покинул чат\n";
            ch.close();
        } else {
            msg = key.attachment() + ": " + sb.toString();
        }

        System.out.println(msg);
        broadcastMessage(msg);
    }

    private void broadcastMessage(String msg) throws IOException {
        ByteBuffer msgBuf = ByteBuffer.wrap(msg.getBytes());
        for (SelectionKey key : selector.keys()) {
            if (key.isValid() && key.channel() instanceof SocketChannel) {
                SocketChannel sch = (SocketChannel) key.channel();
                sch.write(msgBuf);
                msgBuf.rewind();
            }
        }
    }
}
