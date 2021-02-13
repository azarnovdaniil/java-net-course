package ru.daniilazarnov;

import org.apache.log4j.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Содержит основную логику обработки введенных с консоли команд
 */
public class Client {
    private static final Logger LOG = Logger.getLogger(Client.class);
    private static Network client;
    private static final byte FOUR_BYTE = 4;
    private static final byte THREE_INT = 3;
    private static final String PROMPT_TO_ENTER = ">";
    private static final String PROGRAM_NAME = "local_storage ";
    private static final String USERNAME = "~/user1";
    private static final String HOME_FOLDER_PATH = "project/client/local_storage/";
    private static final String WELCOME_MESSAGE = "Добро пожаловать в файловое хранилище!\n"
            + "ver: 0.002a\n"
            + "uod: 09.02.2021\n";

    public static void main(String[] args) throws IOException {
        init();
        mainHandler();
    }

    /**
     * Метод содержит основную логику введенных с консоли команд
     */
    private static void mainHandler() throws IOException {
        String inputLine;
        while (true) {
            InputStream in = System.in;
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            if (client.isConnect()) {
                while (true) {
                    if (!FileSender.isLoadingStatus()) {
                        printPrompt();
                        break;
                    }
                }
                inputLine = bufferedReader.readLine().trim().toLowerCase();
                String firstCommand = inputLine.split(" ")[0];

                switch (firstCommand) {
                    case "up":
                        sendCommand(inputLine);
                        break;
                    case "ls":
                        System.out.println(getFilesList(inputLine));
                        break;
                    case "exit":
                        client.close();
                        System.out.println("Bye");
                        System.exit(0);
                        return;
                    case "connect":
                        System.out.println(connectedToServer());
                        break;
                    case "disconnect":
                        client.close();
                        break;
                    case "down":
                        sendNameFIleForDownloading(inputLine);
                        break;
                    case "help":
                        System.out.println(helpPrint());
                        break;
                    case "status":
                        System.out.println(getStatus());
                        break;
                    case "server":
                        System.out.println(accessingTheServer(inputLine));
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + inputLine);
                }
            }
        }
    }

    /**
     * В этом методе обратимся к серверу за получением списка файлов находящемся в удаленном хранилище
     *
     * @param inputLine ввод;
     */
    private static String accessingTheServer(String inputLine) {
        String result;
        String command;
        if (isThereaThirdElement(inputLine)) { // если после ls введено имя каталога получаем его
            command = getSecondElement(inputLine);
            String folderName = getThirdElement(inputLine);
            if (command.equals("ls")) {
                client.sendStringAndCommand(folderName, FOUR_BYTE);

            } else {
                return "Неизвестная команда";
            }
        } else {
            client.sendStringAndCommand("", FOUR_BYTE);
        }

        result = "Запрос отправлен на сервер";
        return result;
    }

    /**
     * Метод отвечает за вывод справки на консоль
     *
     * @return строку содержащую команды и результат их работы
     */
    private static String helpPrint() {
        return "\n'down' - скачивает файл с сервера\n"
                + "'up' - загружает файл на сервер\n"
                + "'connect' - если соединение разорвано - восстанавливает его\n"
                + "'ls' - вывести имена файлов и каталогов расположенных в корне папки пользователя в локальном "
                + "хранилище\n"
                + "'ls [catalog_name]' - вывести имена файлов и каталогов расположенных в папке "
                + "[catalog_name] на локальном хранилище\n"
                + "'status' - вывести статус подключения к серверу\n"
                + "'server ls [catalog_name]' - вывести имена файлов и каталогов расположенных в папке "
                + "[catalog_name] на удаленном хранилище\n"
                + "'exit' - разорвать соединение и выйти из приложения\n"
                + "пример:\n up fileclient - загрузка файла 'fileclient 'на сервер;"
                + "\n down fileserver - скачивание файла 'fileserver' с сервера;\n"
                + "";
    }

    /**
     * Получает статус состояния соединения с сервером
     *
     * @return возвращает готовую для вывода строку
     */
    private static String getStatus() {
        if (client.isConnect()) {
            return "Сервер доступен";
        } else {
            return "соединение с сервером отсутствует";
        }
    }

    /**
     * Инициализирует соединение с сервером, если оно не еще не активно
     */
    private static String connectedToServer() {
        if (!client.isConnect()) {
            init();
            return "Соединение с сервером получено";
        } else {
            return "Соединение с сервером уже активно";
        }
    }

    /**
     * Получает строку содержащую  файлы и каталоги по указанному пути
     *
     * @param inputLine введеная строка
     * @return результирующая строка имеет вид:  [папка] 'файл';
     */
    private static String getFilesList(String inputLine) {
        String result = "";
        String fileName;
        if (isThereaSecondElement(inputLine)) { // если после ls введено имя каталога получаем его
            fileName = getSecondElement(inputLine);
            if (!Files.isDirectory(Path.of(HOME_FOLDER_PATH + fileName))) {
                return "Файл не является каталогом";
            }
        } else {
            fileName = ""; // если имени каталога нет
        }
        try {
            result = UtilMethod.getFolderContents(fileName, "user");
        } catch (IOException e) {
            LOG.debug(e);
        }
        return result;
    }

    /**
     * Метод выводит на консоль строку приглашение ко вводу
     */
    protected static void printPrompt() {
        System.out.print(PROGRAM_NAME + USERNAME + PROMPT_TO_ENTER);
    }

    private static void init() {
        client = new Network();
        System.out.print(WELCOME_MESSAGE);
    }

    /**
     * Метод берет из второго элемента массива ввода имя файла
     * и если файл существует отправляет его на сервер
     *
     * @param inputLine строка ввода
     */
    private static void sendCommand(String inputLine) {
        if (isThereaSecondElement(inputLine)) {
            String fileName = getSecondElement(inputLine);
            if (isFileExists(fileName)) { // проверяем, существует ли файл
                client.sendFile(HOME_FOLDER_PATH + fileName); // Отправка файла

            } else {
                System.out.println("local_storage: Файл не найден");
            }
        } else {
            System.out.println("local_storage: некорректный аргумент");
        }
    }

    /**
     * Метод отправляет имя файла взятое из введённой в консоль строки на сервер
     * если на сервере есть такой файл, сервер отправляет файл на загрузку.
     */
    private static void sendNameFIleForDownloading(String inputLine) {
        if (isThereaSecondElement(inputLine)) {
            String command = getSecondElement(inputLine);
            client.sendStringAndCommand(command, (byte) 1);
            printPrompt();
        } else {
            System.out.println("local_storage: некорректный управляющий байт");
        }
    }

    /**
     * Метод проверяет есть ли второй элемент в строке
     *
     * @param inputLine входные данные
     * @return если есть второй элемент в строке = true
     */
    private static boolean isThereaSecondElement(String inputLine) {
        return inputLine.split(" ").length == 2;
    }

    /**
     * Метод проверяет есть ли третий элемент в строке
     *
     * @param inputLine входные данные
     * @return если есть третий элемент в строке = true
     */
    private static boolean isThereaThirdElement(String inputLine) {
        return inputLine.split(" ").length == THREE_INT;
    }

    /**
     * Получает второй элемент ввода
     *
     * @param inputLine входящая строка
     * @return второй элемент строки
     */
    private static String getSecondElement(String inputLine) {
        return inputLine.split(" ")[1];
    }

    /**
     * Получает третий элемент ввода
     *
     * @param inputLine входящая строка
     * @return второй элемент строки
     */
    private static String getThirdElement(String inputLine) {
        return inputLine.split(" ")[2];
    }

    /**
     * метод ищет в папке local_storage файл с переданным именем
     *
     * @param fileName имя файла
     * @return истина, если файл в папке обнаружен
     */
    private static boolean isFileExists(String fileName) {
        return Files.exists(Path.of(HOME_FOLDER_PATH + fileName));
    }
}

