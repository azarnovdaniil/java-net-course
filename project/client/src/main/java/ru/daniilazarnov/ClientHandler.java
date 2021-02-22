package ru.daniilazarnov;


import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;
import org.apache.log4j.Logger;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class ClientHandler {
    private static final Logger LOGGER = Logger.getLogger(ClientHandler.class.getName());

    private ObjectEncoderOutputStream objectOut;
    private String clientLogin;

    public ClientHandler(ObjectEncoderOutputStream objectOut) {
        this.objectOut = objectOut;
    }

    public void chooseCommand(String msg) throws IOException {
        String[] msgParts = msg.split("\\s");

        if (msg.startsWith(CommandList.UPLOAD.name().toLowerCase())) {
            LOGGER.info("Загрузка началась");

            try {
                if (Files.exists(Paths.get("./project/clients_dir/" + clientLogin + "/" + msgParts[1]))) {
                    File file = new File("./project/clients_dir/" + clientLogin + "/" + msgParts[1]);
                    int bufSize = 1024 * 1024 * 10;
                    int partsCount = (int) (file.length() / bufSize);
                    if (file.length() % bufSize != 0) {
                        partsCount++;
                    }

                    FileMsg fm = new FileMsg(file.getName(), -1, partsCount, new byte[bufSize], clientLogin);
                    FileInputStream in = new FileInputStream(file);

                    LOGGER.debug("Загрузка началась");
                    for (int i = 0; i < partsCount; i++) {
                        int readedBytes = in.read(fm.getData());
                        fm.setPartNumber(i + 1);
                        if (readedBytes < bufSize) {
                            fm.setData(Arrays.copyOfRange(fm.getData(), 0, readedBytes));
                        }
                        objectOut.writeObject(fm);
                        objectOut.flush();
                        LOGGER.debug("Часть: " + (i + 1) + " отправлена");
                    }
                    in.close();
                    LOGGER.debug("Файл полностью загружен");
                }
            } catch (IOException e) {
                LOGGER.error("Сбой при загрузке файла");
                throw new RuntimeException("Что-то пошло не так...", e);
            }
        } else if (msg.startsWith(CommandList.DOWNLOAD.name().toLowerCase())) {
            LOGGER.info("Скачивание началось");
            RequestMsg rm = new RequestMsg(CommandList.DOWNLOAD, msgParts[1], clientLogin);
            objectOut.writeObject(rm);
            objectOut.flush();
        } else if (msg.startsWith(CommandList.LIST.name().toLowerCase())) {
            LOGGER.info("Ожидание списка файлов с сервера");
            RequestMsg rm = new RequestMsg(CommandList.LIST, clientLogin);
            objectOut.writeObject(rm);
            objectOut.flush();
        } else if (msg.startsWith(CommandList.REMOVE.name().toLowerCase())) {
            LOGGER.info("Файл удален");
            RequestMsg rm = new RequestMsg(CommandList.REMOVE, msgParts[1], clientLogin);
            objectOut.writeObject(rm);
            objectOut.flush();
        } else if (msg.startsWith(CommandList.RENAME.name().toLowerCase())) {
            LOGGER.info("Изменение имени файла");
            RequestMsg rm = new RequestMsg(CommandList.RENAME, msgParts[1], msgParts[2], clientLogin);
            objectOut.writeObject(rm);
            objectOut.flush();
        } else if (msg.startsWith(CommandList.AUTH.name().toLowerCase())) {
            LOGGER.info("Ожидание сервера аутентификации");
            DBMsg dbm = new DBMsg(CommandList.AUTH, msgParts[1], msgParts[2]);
            objectOut.writeObject(dbm);
            objectOut.flush();
        } else if (msg.startsWith(CommandList.REG.name().toLowerCase())) {
            LOGGER.info("Регистрация нового пользователя");
            DBMsg dbm = new DBMsg(CommandList.REG, msgParts[1], msgParts[2]);
            objectOut.writeObject(dbm);
            objectOut.flush();
        } else if (msg.startsWith(CommandList.HELP.name().toLowerCase())) {
            helpInfo();
        } else LOGGER.info("Неопознанная команда");
    }

    public void setClientLogin(String clientLogin) {
        this.clientLogin = clientLogin;
    }

    private static void helpInfo() {
        LOGGER.info("Добро пожаловать!");
        LOGGER.info("Список комманд: ");
        LOGGER.info("reg - Если вы не еще зарегестрированы- пожалуйств создайте учетную запись! (reg user1 111)");
        LOGGER.info("auth - авторизация (auth user1 1)");
        LOGGER.info("upload - Загрузка файлов с вашего каталога на сервер (upload 1.txt)");
        LOGGER.info("download - Скачивание файла из хранилища в ваш каталог (download 2.txt)");
        LOGGER.info("list - Список ваших файлов в сетевом хранилище на сервере (list:...)");
        LOGGER.info("remove - Удалить файлы на сервере (remove test1.txt t2.txt)");
        LOGGER.info("rename -  Изменить имя файла в харнилище (rename test1.txt test11.txt)");
        LOGGER.info("exit - закрыть приложение");
    }
}
