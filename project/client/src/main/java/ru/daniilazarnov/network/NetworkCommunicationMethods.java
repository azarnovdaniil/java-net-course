package ru.daniilazarnov.network;

import ru.daniilazarnov.auth.Auth;
import ru.daniilazarnov.Command;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import static ru.daniilazarnov.string_method.StringMethod.*;
import static ru.daniilazarnov.string_method.StringMethod.getSecondElement;

public class NetworkCommunicationMethods {
    private static final String HOME_FOLDER_PATH = Path.of("project", "client", "local_storage").toString();
    private static Client client;


    /**
     * В этом методе обратимся к серверу за получением списка файлов находящемся в удаленном хранилище
     *
     * @param inputLine ввод;
     */
    public static String accessingTheServer(String inputLine) {
        String result;
        if (isThereaThirdElement(inputLine)) { // если после ls введено имя каталога получаем его
            String folderName = getThirdElement(inputLine);
            Command command = Command.valueOf(getThirdElement(inputLine).toUpperCase());
            if (command == Command.LS) {
                sendStringAndCommandByte(folderName, Command.LS.getCommandByte());
            } else {
                return "Неизвестная команда";
            }
        } else {
            sendStringAndCommandByte("", Command.LS.getCommandByte());
        }
        result = "Запрос отправлен на сервер";
        return result;
    }

    public static void sendStringAndCommandByte(String folderName, byte commandByte) {
        client.sendStringAndCommand(folderName, commandByte);
    }

    public static boolean isConnect() {
        return client.isConnect();
    }

    public static boolean auth() {
        Auth auth = new Auth();
        return auth.auth();
    }

    public static void init() {
        client = new Client();

    }

    /**
     * Инициализирует соединение с сервером, если оно не еще не активно
     */
    public static String connectedToServer() {
        if (!client.isConnect()) {

            init();
            return "Соединение с сервером получено";
        } else {
            return "Соединение с сервером уже активно";
        }
    }

    /**
     * Получает статус состояния соединения с сервером
     *
     * @return возвращает готовую для вывода строку
     */
    public static String getStatus() {
        if (client.isConnect()) {
            return "Сервер доступен";
        } else {
            return "соединение с сервером отсутствует";
        }
    }

    /**
     * Метод отправляет имя файла взятое из введённой в консоль строки на сервер
     * если на сервере есть такой файл, сервер отправляет файл на загрузку.
     */
    public static void sendNameFIleForDownloading(String inputLine) {
        if (isThereaSecondElement(inputLine)) {
            String command = getSecondElement(inputLine);
            sendStringAndCommandByte(command, (byte) 1);
        } else {
            System.out.println("local_storage: некорректный управляющий байт");
        }
    }

    /**
     * Метод берет из второго элемента массива ввода имя файла
     * и если файл существует отправляет его на сервер
     *
     * @param inputLine строка ввода
     */
    public static void sendFile(String inputLine) {
        if (isThereaSecondElement(inputLine)) {
            String fileName = getSecondElement(inputLine);

            System.out.println(isFileExists(fileName));
            if (isFileExists(fileName)) { // проверяем, существует ли файл
                client.sendFile(HOME_FOLDER_PATH + File.separator + fileName); // Отправка файла

            } else {
                System.out.println("local_storage: Файл не найден");
            }
        } else {
            System.out.println("local_storage: некорректный аргумент");
        }
    }

    /**
     * метод ищет в папке local_storage файл с переданным именем
     *
     * @param fileName имя файла
     * @return истина, если файл в папке обнаружен
     */

    private static boolean isFileExists(String fileName) {
        return Files.exists(Path.of(HOME_FOLDER_PATH, fileName));
    }

    public static void close() {
        client.close();
    }
}

