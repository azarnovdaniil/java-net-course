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
        this.serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT); // регистрация селектора на каннале с определенным ключом акцепт
    }

    public static void main(String[] args) throws IOException {
        start();
    }

    private static void start() throws IOException {
        new Thread(new Server()).start(); // запускаем сервер
    }

    @Override
    public void run() {
        try {
            log.info("Сервер запущен (Порт: 8189)");
            Iterator<SelectionKey> iter;
            SelectionKey key;
            while (this.serverSocketChannel.isOpen()) { // внутри вайла канал открыт
                selector.select();// ждем новых событий
                iter = this.selector.selectedKeys().iterator();
                while (iter.hasNext()) { // перебираем ключи в итераторе событий
                    key = iter.next(); //получаем ссылку на событие
                    iter.remove(); //удаляем событие из списка обработки
                    if (key.isAcceptable()) { // проверяем ключ
                        handleAccept(key); // если подключился
                    }
                    if (key.isReadable()) { //если что-то написал
                        handleRead(key);
                    }
                }
            }
        } catch (IOException e) {

            log.error(e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    /**
     * Метод подключает новых клиентов
     *
     * @param key Ключ выбора создается каждый раз, когда канал регистрируется с помощью селектора.
     *            Ключ остается действительным до тех пор, пока он не будет отменен вызовом его метода cancel
     * @throws IOException исключение
     */
    private void handleAccept(SelectionKey key) throws IOException {
        SocketChannel sc = ((ServerSocketChannel) key.channel()).accept(); // получаем канал из ключа, ссылка на ССК
        String clientName = "client #" + acceptedClientIndex; // присваеваем индекс клиенту
        acceptedClientIndex++;
        sc.configureBlocking(false); // переключение на неблокирующий режим
        sc.register(selector, SelectionKey.OP_READ, clientName);// регистрируем ключ в селекторе на чтение и предаем вместе с именем клиента
        sc.write(welcomeBuf); // посылаем сообщение из велком буфера
        welcomeBuf.rewind(); //сбрасывает позицию буфера на начало, для повторной отправки
        log.info("Подключился новый клиент " + clientName);
    }


    /**
     * Обработка события чтение из канала
     * Чтение из канала
     *
     * @param key ключ
     * @throws IOException исключени
     */
    private void handleRead(SelectionKey key) throws IOException {
        SocketChannel ch = (SocketChannel) key.channel(); //получаем ссылку на канал из ключа
        StringBuilder sb = new StringBuilder();

        buf.clear(); //очищаем буфер
        int read;
        try {
            while ((read = ch.read(buf)) > 0) { //читаем из канала
                buf.flip();
                byte[] bytes = new byte[buf.limit()];
                buf.get(bytes); //записываем в данные из буфера в массив
                sb.append(new String(bytes)); // добавляем в стринг билдер
                buf.clear();

            }
        } catch (Exception e) {
            key.cancel();
            read = -1;
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


    /**
     * Рассылаем сообщения всем клиентам
     *
     * @param msg сообщение
     * @throws IOException
     */
    private void broadcastMessage(String msg) throws IOException {
        ByteBuffer msgBuf = ByteBuffer.wrap(msg.getBytes()); // оборачиваем собщение в байтбуффер
        for (SelectionKey key : selector.keys()) { // перебираем список всех ключей подписавшихся
            if (key.isValid() && key.channel() instanceof SocketChannel) { // если ключи валидные и канал является соектканалом
                SocketChannel sch = (SocketChannel) key.channel(); // получаем ссылку
                sch.write(msgBuf); //отправляем сообщение
                msgBuf.rewind(); // возвращаем буфер в исходное положение
            }
        }
    }
}
