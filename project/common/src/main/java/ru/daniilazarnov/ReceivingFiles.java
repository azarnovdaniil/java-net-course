package ru.daniilazarnov;

import io.netty.buffer.ByteBuf;
import ru.daniilazarnov.enumeration.State;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static ru.daniilazarnov.constants.Constants.*;

/**
 * Класс реализующий получение файла по протоколу
 * [] - 1b управляющий байт
 * [][][][] - 1 int длинна имени файла
 * [] - byte[?] имя файла
 * [][][][][][][][] long размер файла в байтах
 * [] data[] - содержимое файла
 */
public class ReceivingFiles {
    private static final String USER = "user1";
    private static final Byte FOUR_BYTES = 4;
    private static final byte EIGHTS_BYTES = 8;
    private static State currentState = State.IDLE;

    private static final String PREFIX_FILE_NAME = File.separator + "_";
    private static final String SERVER_NAME = "server";
    private static int nextLength;
    private static long fileLength;
    private static long receivedFileLength;
    private static BufferedOutputStream out;

    public static State getCurrentState() {
        return currentState;
    }

    /**
     * Метод реализующий получение файла по протоколу
     *
     * @param msg  - данные
     * @param user - пользователь (сервер или клиент) параметр нужен для того что бы
     *             корректно составить путь для сохранения файла
     */
    public static void fileReceive(Object msg, String user) throws IOException {
        // собираем путь до целевой папки
        String fileNameStr;
        String fullPathString = user.equals(SERVER_NAME)
                ? DEFAULT_PATH_SERVER + File.separator + USER // собираем путь до целевой папки
                + PREFIX_FILE_NAME : DEFAULT_PATH_USER
                + PREFIX_FILE_NAME;

        ByteBuf buf = ((ByteBuf) msg);

        if (currentState == State.IDLE) {
            receivedFileLength = 0;
            buf.resetReaderIndex();
            byte readed = buf.readByte();
            if (readed == (byte) 2) {
                currentState = State.NAME_LENGTH;
                System.out.println("currentState changed: " + currentState);
                System.out.println("Client: Start file receiving");
            } else {
                System.out.println("(class ReceivingFiles)ERROR: Invalid first byte - " + readed);
                return;
            }
        }

        if (currentState == State.NAME_LENGTH) {
            if (buf.readableBytes() < FOUR_BYTES) {
                return;
            }
            if (buf.readableBytes() >= FOUR_BYTES) {
                System.out.println("STATE: Get filename length");
                nextLength = buf.readInt();
                currentState = State.NAME;
            }
        }

        if (currentState == State.NAME) {
            if (buf.readableBytes() < nextLength) {
                return;
            }
            if (buf.readableBytes() >= nextLength) {
                byte[] fileName = new byte[nextLength];
                buf.readBytes(fileName);
                fileNameStr = new String(fileName);
                System.out.println("STATE: Filename received - _" + new String(fileName));
                out = new BufferedOutputStream(new FileOutputStream(fullPathString + fileNameStr));
                currentState = State.FILE_LENGTH;
            }
        }

        if (currentState == State.FILE_LENGTH) {
            if (buf.readableBytes() < EIGHTS_BYTES) {
                return;
            }
            if (buf.readableBytes() >= EIGHTS_BYTES) {
                fileLength = buf.readLong();
                System.out.println("STATE: File length received - " + fileLength);
                currentState = State.FILE;
            }
        }
        if (currentState == State.FILE) {
            while (buf.readableBytes() > 0) {
                out.write(buf.readByte()); //Записываем в цикле напрямую в файл
                receivedFileLength++;
                if (fileLength == receivedFileLength) {
                    currentState = State.IDLE;
                    System.out.println("File received");
                    if (buf.readableBytes() == 0) {
                        buf.clear();
                    }
                    out.close();
                }
            }
        }
    }
}
