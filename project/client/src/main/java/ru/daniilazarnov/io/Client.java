package ru.daniilazarnov.io;

import ru.daniilazarnov.ObjectSerialization;
import ru.daniilazarnov.files.FileSerializable;
import ru.daniilazarnov.oper.Operations;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Properties;

import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;

public class Client {
    private static final String SERVER_PORT = "server.port";
    private static final String SERVER_URL = "server.url";
    private static final int READ_COUNT_BYTE = 10;

    private static Properties prop = new Properties();
    private OutputStream out;
    private InputStream in;

    static {
        try {
            prop.load(new FileInputStream("/Users/arturmalcev/IdeaProjects/NetworkStorage/project/client/src/main/resources/conf.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Client() {
        try (Socket socket = new Socket(prop.getProperty(SERVER_URL), Integer.parseInt(prop.getProperty(SERVER_PORT)))) {
            this.in = socket.getInputStream();
            this.out = socket.getOutputStream();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        //Запускаем поток чтения сообщений от сервера
        Thread readMessFromServer = new Thread(() -> {
            while (true) {
                //Читаем сообщение и производим десериализацию.
                try (ObjectInput objectInput = new ObjectDecoderInputStream(in)) {
                    ObjectSerialization objSer = (ObjectSerialization) objectInput.readObject();
                    switch (objSer.getOper()) {
                        case DOWNLOAD: {
                            saveFileFromServer(objSer.getFile());
                            break;
                        }
                        case DIR: {
                            break;
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        readMessFromServer.setDaemon(true);
        readMessFromServer.start();

        //Запускаем бесконечный цикл нашей клиентской части
        while (true) {

        }
    }

    //Метод скачивания файла и его частей с сервера
    private void saveFileFromServer(FileSerializable file) throws IOException {
        Path path = Paths.get(file.getPath());

        if (Files.notExists(path)) {
            Files.write(path, file.getArr(), StandardOpenOption.CREATE_NEW);
        } else {
            Files.write(path, file.getArr(), StandardOpenOption.APPEND);
        }
    }

    //Метод отправки файла на сервер
    private void sendFileToServer(Path path) {
        try (InputStream inputStream = new FileInputStream(path.toFile());
             ObjectOutput objOut = new ObjectEncoderOutputStream(out)) {
            //Создаем массив для чтения файла
            byte[] arr = new byte[READ_COUNT_BYTE];

            int part = 0;
            //Считаем количества частей файла
            int partCount = (int) Math.ceil(Files.size(path) / (double) READ_COUNT_BYTE);

            while (inputStream.available() > 0) {
                part++;
                //Если остался хвост файла меньше размера наших частей
                if (inputStream.available() < READ_COUNT_BYTE) {
                    byte[] b = new byte[in.available()];
                    inputStream.read(b);
                    arr = b;
                } else {
                    inputStream.read(arr, 0, READ_COUNT_BYTE);
                }

                ObjectSerialization file = new ObjectSerialization(Operations.UPLOAD
                        , new FileSerializable(path.toString(), Files.size(path), part, partCount, arr)
                        , null);

                //Сереализуем и отправляем на сервер
                objOut.writeObject(file);
                objOut.flush();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
