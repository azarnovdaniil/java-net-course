package ru.daniilazarnov;

import org.apache.log4j.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

/**
 * Содержит основную логику обработки введенных с консоли команд
 */
public class Client {
    private static final Logger LOG = Logger.getLogger(Client.class);
    private static Network client;
    private static final int DELAY = 10;
    private static final byte FOUR_BYTE = 4;
    private static final byte THREE_BYTE = 3;
    private static final String PROMPT_TO_ENTER = ">";
    private static final String PROGRAM_NAME = "local_storage ";
    private static final String USERNAME = "~" + File.separator + "user1";
    private static final String HOME_FOLDER_PATH = Path.of("project", "client", "local_storage")
            .toString() + File.separator;
    private static final String WELCOME_MESSAGE = "Добро пожаловать в файловое хранилище!\n"
            + "ver: 0.002a\n"
            + "uod: 09.02.2021\n";

    public static void main(String[] args) throws IOException {
        init();
        auth();
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
                Command command;
                try {
                    command = Command.valueOf(firstCommand.toUpperCase());


                    switch (command) {
                        case AUTH:
//                        auth(inputLine);
                            break;
                        case UPLOAD:
                            sendCommand(inputLine);
                            break;
                        case LS:
                            System.out.println(getFilesList(inputLine));
                            break;
                        case EXIT:
                            client.close();
                            System.out.println("Bye");
                            System.exit(0);
                        case CONNECT:
                            System.out.println(connectedToServer());
                            break;
                        case DISCONNECT:
                            client.close();
                            break;
                        case DOWNLOAD:
                            sendNameFIleForDownloading(inputLine);
                            break;
                        case HELP:
                            System.out.println(Command.getHelpInfo());
                            break;
                        case STATUS:
                            System.out.println(getStatus());
                            break;
                        case SERVER:
                            System.out.println(accessingTheServer(inputLine));
                            printPrompt();
                            break;
                        default:
                            LOG.error("Unexpected value: " + inputLine);
                    }
                } catch (IllegalArgumentException e) {
                    System.err.println("Некорректная команда");
                    LOG.error(e);
                    mainHandler();
                }

            }
        }
    }

    private static boolean auth() {
        InputStream in = System.in;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));

        String userName = "";
        char[] password = new char[0];

        while (!client.isConnect()) {
            try {
                Thread.sleep(DELAY);
            } catch (InterruptedException e) {
                LOG.error(e);
            }
        }

        Console console = System.console();
        if (console == null) {
            System.out.println("Логин: ");
            try {
                userName = bufferedReader.readLine().trim();
                System.out.println("Пароль: ");
                password = bufferedReader.readLine().trim().toCharArray();

                System.out.println(userName + " - " + Arrays.toString(password));

            } catch (IOException e) {
                LOG.error(e);
            }
        } else {
            System.out.println("Enter username: ");
            userName = console.readLine("Username: ");
            password = console.readPassword("Password: ");
        }


        String passString = new String(password);
        client.sendStringAndCommand((userName + "%-%" + passString), Command.AUTH.getCommandByte());


        return false;
    }

    /**
     * В этом методе обратимся к серверу за получением списка файлов находящемся в удаленном хранилище
     *
     * @param inputLine ввод;
     */
    private static String accessingTheServer(String inputLine) {
        String result;
        if (isThereaThirdElement(inputLine)) { // если после ls введено имя каталога получаем его
            String folderName = getThirdElement(inputLine);
            Command command = Command.valueOf(getThirdElement(inputLine).toUpperCase());
            if (command == Command.LS) {
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
        try {
            Thread.sleep(DELAY);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
        return inputLine.split(" ").length == THREE_BYTE;
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

