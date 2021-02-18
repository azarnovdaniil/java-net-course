package ru.daniilazarnov.network;

import ru.daniilazarnov.enumeration.Command;
import ru.daniilazarnov.console_IO.OutputConsole;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import static ru.daniilazarnov.string_method.StringMethod.*;
import static ru.daniilazarnov.constants.Constants.*;
import static ru.daniilazarnov.string_method.StringMethod.getSecondElement;

public class NetworkCommunicationMethods {
    private static Client client;

    /**
     * В этом методе обратимся к серверу за получением списка файлов находящемся в удаленном хранилище
     *
     * @param inputLine ввод;
     */
    public String accessingTheServer(String inputLine) {
        String result;
        if (isThereaThirdElement(inputLine)) { // если после ls введено имя каталога получаем его
            String folderName = getThirdElement(inputLine);
            Command command = Command.valueOf(getThirdElement(inputLine).toUpperCase());
            if (command == Command.LS) {
                OutputConsole.setConsoleBusy(true);
                sendStringAndCommandByte(folderName, Command.LS.getCommandByte());
            } else {
                return "Неизвестная команда";
            }
        } else {
            sendStringAndCommandByte("", Command.LS.getCommandByte());
        }
        result = "local_storage: Запрос отправлен на сервер";
        return result;
    }

    public void sendStringAndCommandByte(String folderName, byte commandByte) {
        client.sendStringAndCommand(folderName, commandByte);
    }

    public static boolean isConnect() {
        return client.isConnect();
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
    public void sendNameFIleForDownloading(String inputLine) {
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
    public static String sendFile(String inputLine) {
        String result = "local_storage: ";
        if (isThereaSecondElement(inputLine)) {
            String fileName = getSecondElement(inputLine);

            if (isFileExists(fileName)) { // проверяем, существует ли файл
                OutputConsole.setConsoleBusy(true);
                client.sendFile(DEFAULT_PATH_USER + File.separator + fileName); // Отправка файла
                result += "Файл отправлен";

            } else {
                result += "Файл не найден";
            }
        } else {
            System.out.println("local_storage: некорректный аргумент");
            result += " некорректный аргумент";
        }
        return result;
    }

    public static void exit() {
        close();
        System.out.println("Bye");
        System.exit(0);
    }

    private static boolean isFileExists(String fileName) {
        return Files.exists(Path.of(DEFAULT_PATH_USER, fileName));
    }

    public static void close() {
        client.close();
    }
}
