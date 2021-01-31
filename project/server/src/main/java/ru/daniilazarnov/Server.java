package ru.daniilazarnov;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;

public class Server implements Runnable {
    private static final Logger log = Logger.getLogger(Server.class);
    private static ServerSocketChannel serverSocketChannel = null;
    private final Selector selector; // слушает все события
    private int acceptedClientIndex = 1;

    Server() throws IOException {
       serverSocketChannel = ServerSocketChannel.open(); //создаем ССЧ
       serverSocketChannel.socket().bind(new InetSocketAddress(8189)); //устанавливаем порт для ССЧ, который он будет слушать
       serverSocketChannel.configureBlocking(false);
       selector = Selector.open();
       serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT); // регистрация селектора на каннале с определенным ключом акцепт
    }

    public static void main(String[] args) throws IOException {
        start();
    }

    /**
     * Запись полученных данных в файл
     *
     * @param ch   канал получаемых данных
     * @param size размер получаемых данных
     * @throws IOException исключение
     */
    private static void writeFile(SocketChannel ch, SelectionKey key, byte size) throws IOException {
        String file = "data/_nio-data.txt";
        Path filePath = Paths.get(file);
        try (FileChannel fileChannel = FileChannel.open(Paths.get("data/_nio-data.txt"), StandardOpenOption.CREATE,
                StandardOpenOption.WRITE)) {
            fileChannel.transferFrom(ch, 0, size);
            sendMessage(key, "SERVER: file received successfully\n");
            log.debug("Files.exists(filePath): " + Files.exists(filePath));
        }
    }

    private static void sendMessage(SelectionKey key, String msg) throws IOException {
        ByteBuffer msgBuf = ByteBuffer.wrap(msg.getBytes());
        SocketChannel sch = (SocketChannel) key.channel();
                sch.write(msgBuf);
                msgBuf.rewind();
    }

    /**
     * Стартуем сервер
     *
     * @throws IOException исключение
     */
    private static void start() throws IOException {
        new Thread(new Server()).start(); // запускаем сервер
    }

    @Override
    public void run() {
        try {
            log.info("Сервер запущен (Порт: 8189)");
            Iterator<SelectionKey> iter;
            SelectionKey key;
            while (serverSocketChannel.isOpen()) { // внутри вайла канал открыт
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
        sendMessage(sc.keyFor(selector), "SERVER: Welcome to chat!\n");
        log.info("Подключился новый клиент " + clientName);
    }

    /**
     * Обработка события чтение из канала
     * Чтение из канала
     *
     * @param key ключ
     * @throws IOException исключение
     */
    private void handleRead(SelectionKey key) throws IOException {
        SocketChannel ch = (SocketChannel) key.channel(); //получаем ссылку на канал из ключа
        StringBuilder sb = new StringBuilder();
        byte control = getControlByte(ch);
        byte sizeFile = getSizeFile(ch);

        switch (control) {
            case 70:
                writeFile(ch, key, sizeFile);
                break;
            case 71:
                getMessage(key, ch, sb);
                break;

            default:
                log.error("Unexpected value: " + control);
//                throw new IllegalStateException("Unexpected value: " + control);
        }
    }

    /**
     * Получаем сообщение
     *
     * @param key ключ
     * @param ch  канал
     * @param sb  стрингБилдер
     * @throws IOException исключение
     */
    private void getMessage(SelectionKey key, SocketChannel ch, StringBuilder sb) throws IOException {
        ByteBuffer buf = ByteBuffer.allocate(8);
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
     * Через этот метод получаем размер файла в байтах из второго байта, метод должен использоваться после
     *
     * @param ch канал приема
     * @return размер получаемого файла
     * @throws IOException исключение
     * @getControlByte иначе будет ошибка в получении служебной информации
     */
    private byte getSizeFile(SocketChannel ch) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(1);
        byte size = -1;
        int read;
        buffer.clear(); //очищаем буфер
        if ((read = ch.read(buffer)) > 0) {
            buffer.flip();
            byte[] bytes = new byte[1];
            buffer.get(bytes); //записываем в данные из буфера в массив
            size = bytes[0];
        }
        return size;
    }

    /**
     * Получаем первый байт сообщения, который содержит данные о передаваемом контенте.
     *
     * @param ch канал получаемых данных
     * @return управляющий байт;
     * @throws IOException исключение
     */
    private byte getControlByte(SocketChannel ch) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(1);
        byte control = -1;
        int read;
        buffer.clear(); //очищаем буфер
        if ((read = ch.read(buffer)) > 0) {
            buffer.flip();
            byte[] bytes = new byte[1];
            buffer.get(bytes); //записываем в данные из буфера в массив
            control = bytes[0];
        }
        return control;
    }

    /**
     * Рассылаем сообщения всем клиентам
     *
     * @param msg сообщение
     * @throws IOException исключение
     */
    private void broadcastMessage(String msg) throws IOException {
        for (SelectionKey key : selector.keys()) { // перебираем список всех ключей подписавшихся
            if (key.isValid() && key.channel() instanceof SocketChannel) { // если ключи валидные и канал является соектканалом
                sendMessage(key, msg);
            }
        }
    }
}
