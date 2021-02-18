package ru.daniilazarnov;

import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.*;
import java.net.Socket;
import java.nio.file.*;

public class Client {
    private static final Logger LOGGER = Logger.getLogger(Client.class.getName());

    private String host;
    private int port;
    private String login;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public static void main(String[] args) {
        PropertyConfigurator.configure("./project/client/log4j.properties");
        LOGGER.debug("Запуск клиента...");
        new Client("localhost", 8189).run();
    }

    public void run() {
        try (Socket socket = new Socket(host, port);
             ObjectEncoderOutputStream objectOut = new ObjectEncoderOutputStream(socket.getOutputStream());
             ObjectDecoderInputStream objectIn = new ObjectDecoderInputStream(socket.getInputStream(), 1024 * 1024 * 100)) {
            LOGGER.debug("Клиент успешно подключился");

            ClientHandler clientHandler = new ClientHandler(objectOut);

            new Thread(() -> {
                try {
                    while (true) {
                        AbstractMsg receivedFile = (AbstractMsg) objectIn.readObject();
                        LOGGER.debug("Сообщение доставлено");

                        if (receivedFile instanceof FileMsg) {
                            FileMsg fm = (FileMsg) receivedFile;
                            LOGGER.debug("Получен файл: " + fm.getPartNumber() + " / " + fm.getPartsCount());

                            boolean append = true;
                            if (fm.getPartNumber() == 1) {
                                append = false;
                            }

                            File newFile = new File("./project/clients_dir/" + login + "/" + fm.getFilename());
                            FileOutputStream fos = new FileOutputStream(newFile, append);

                            fos.write(fm.getData());
                            fos.close();
                        } else if (receivedFile instanceof DirectoryListInfo) {
                            DirectoryListInfo dim = (DirectoryListInfo) receivedFile;
                            LOGGER.debug("Получена информация о расположении файла");
                            LOGGER.info("Файлы в каталоге: ");
                            System.out.println(dim.getFilesAtDirectory().toString());
                        } else if (receivedFile instanceof DBMsg) {
                            DBMsg dbm = (DBMsg) receivedFile;
                            LOGGER.debug("Получена информация от базы данных");
                            LOGGER.info("Авторизация прошла успешно");
                            login = dbm.getLogin();
                            clientHandler.setClientLogin(login);
                            Path newClientDir = Paths.get("./project/clients_dir/" + dbm.getLogin());
                            if (!newClientDir.toFile().exists()) {
                                Files.createDirectories(newClientDir);
                            }
                        }
                    }
                } catch (IOException | ClassNotFoundException e) {
                    LOGGER.info("Соединение закрыто");
                }
            }).start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            LOGGER.info("Теперь вы можете писать команды, соединение прошло успешно");
            LOGGER.info("Не забудьте авторизоваться. Используйте /help, для просмотра доступных команд");
            while (true) {
                try {
                    String msg = reader.readLine();
                    if (msg.startsWith("/exit")) {
                        socket.close();
                        break;
                    }
                    clientHandler.chooseCommand(msg);
                } catch (IOException e) {
                    throw new RuntimeException("Что-то пошло не так...", e);
                }
            }

        } catch (Exception e) {
            LOGGER.error("Что-то пошло не так...", e);
            throw new RuntimeException("Что-то пошло не так...", e);
        }
    }

}