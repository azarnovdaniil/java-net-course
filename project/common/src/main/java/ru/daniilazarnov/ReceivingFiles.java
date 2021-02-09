package ru.daniilazarnov;

import io.netty.buffer.ByteBuf;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * Класс реализующий получение файла по протоколу
 *     [] - 1b управляющий байт
 *     [][][][] - 1 int длинна имени файла
 *     [] - byte[?] имя файла
 *     [][][][][][][][] long размер файла в байтах
 *     [] data[] - содержимое файла
 */
public class ReceivingFiles {
    private static State currentState = State.IDLE;
    private static final String DEFAULT_PATH_SERVER = "project/server/cloud_storage/";
    private static final String DEFAULT_PATH_USER = "project/client/local_storage/";
    private static final String prefixFileName = "/_";
    private static final String serverName = "server";
    private static int nextLength;
    private static long fileLength;
    private static long receivedFileLength;
    private static BufferedOutputStream out;

    /**
     * Метод реализующий получение файла по протоколу
     *
     * @param msg - данные
     * @param user - пользователь (сервер или клиент) параметр нужен для того что бы
     *             корректно составить путь для сохранения файла
     */
    public static void fileReceive(Object msg, String user) throws IOException {
        // собираем путь до целевой папки
        final String FULL_PATH_TO_FILE = user.equals(serverName) ? // собираем путь до целевой папки
                DEFAULT_PATH_SERVER +
                        "user1" +
                        prefixFileName :
                DEFAULT_PATH_USER + prefixFileName
                        .replace("/", "");


        ByteBuf buf = ((ByteBuf) msg);
        if (currentState == State.IDLE) {
                buf.resetReaderIndex();
            byte readed = buf.readByte();
            if (readed == (byte) 25) {
                currentState = State.NAME_LENGTH;
                System.out.println("Client: Start file receiving");
            } else {
                System.out.println("(class ReceivingFiles)ERROR: Invalid first byte - " + readed);
                return;

            }
        }

        if (currentState == State.NAME_LENGTH) {
            if (buf.readableBytes() < 4) return;
            if (buf.readableBytes() >= 4) {
                System.out.println("STATE: Get filename length");
                nextLength = buf.readInt();
                currentState = State.NAME;
            }
        }

        if (currentState == State.NAME) {
            if (buf.readableBytes() < nextLength) return;
            if (buf.readableBytes() >= nextLength) {
                byte[] fileName = new byte[nextLength];
                buf.readBytes(fileName);
                System.out.println("STATE: Filename received - _" + new String(fileName));
                out = new BufferedOutputStream(new FileOutputStream(FULL_PATH_TO_FILE + new String(fileName)));
                currentState = State.FILE_LENGTH;
            }
        }

        if (currentState == State.FILE_LENGTH) {
            if (buf.readableBytes() < 8) return;
            if (buf.readableBytes() >= 8) {
                fileLength = buf.readLong();
                System.out.println("STATE: File length received - " + fileLength);
                currentState = State.FILE;
            }
        }

        if (currentState == State.FILE) {
            while (buf.readableBytes() > 0) {//Записываем в цикле напрямую в файл
                out.write(buf.readByte());
                receivedFileLength++;
                if (fileLength == receivedFileLength) {
                    currentState = State.IDLE;
                    System.out.println("File received");
                    out.close();
                }
            }
        }
        buf.clear();
    }


}
