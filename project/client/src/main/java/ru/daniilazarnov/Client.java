package ru.daniilazarnov;


import org.apache.log4j.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;


   /*
    Нереализованный протокол:
            [byte [] ] 1b управляющий байт →
            [short [][]] 2b длинна имени файла →
            [byte[]?] nb  имя файла →
            [long  [][][][][][][][]] 8b размер файла →
            [byte[]?] nb содержимое файла

//                       System.out.println("Files.exists: " + Files.exists(Path.of("project/client/local_storage")));
//                       client.sendfile("project/client/local_storage/file_to_send.txt");
//                       sendMsg(inputLine);

     */

public class Client {
    private static final Logger log = Logger.getLogger(Client.class);
    private static Network client;
    private static BufferedReader bufferedReader = null;
    //    private static String msg = "◙◙◙";
    public static final String PROMPT_TO_ENTER = ">";
    public static final String PROGRAM_NAME = "local_storage ";
    public static final String USERNAME = "~/user1";
    public static final String HOME_FOLDER_PATH = "project/client/local_storage/";
    public static final String WELCOME_MESSAGE = "Добро пожаловать в файловое хранилище!\n" +
            "ver: 0.001a\n" +
            "uod: 05.02.2021\n";


    public static void main(String[] args) throws IOException {
        init();

        String inputLine;
        while (true) {
            InputStream in = System.in;
            bufferedReader = new BufferedReader(new InputStreamReader(in));
            if (client.isConnect()) {
                printPrompt();
                inputLine = bufferedReader.readLine().trim().toLowerCase();
                String firstCommand = inputLine.split(" ")[0];

                switch (firstCommand) {
                    case "send":
                        sendCommand(inputLine);
                        break;
                    case "ls":
                        System.out.println(getFilesList(inputLine));
                        break;
                    case "exit":
                        client.close();
                        System.exit(0);
                        return;
                    case "connect":
                        connectedToServer();
                        break;
                    case "disconnect":
                        client.close();
                        break;
                    case "up":
                        sendNameFIleForDownloading(inputLine);
                        break;

                    case "status":
                        if (client.isConnect()) System.out.println("Сервер доступен");
                        else System.err.println("соединение с сервером отсутствует");
//                        sendMsg("status");
                        break;

                    default:
                        throw new IllegalStateException("Unexpected value: " + inputLine);
                }
            }
        }
    }

    private static void connectedToServer() {
        if (!client.isConnect()) {
            init();
        } else {
            System.out.println("Соединение с сервером активно");
        }
    }

    private static String getFilesList(String inputLine) {
        String result = "";
        String fileName;
        if (isThereaSecondElement(inputLine)) {
            fileName = getSecondElement(inputLine);
            if (!Files.isDirectory(Path.of(HOME_FOLDER_PATH + fileName))) {
                System.out.println("Файл не является каталогом");
            }
        } else fileName = "";


        try {
            result = UtilMethod.getFolderContents(fileName);
        } catch (IOException e) {
            log.debug(e);
        }
        return result;
    }

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
                if (client.isConnect()) printPrompt();
            } else {
                System.out.println("local_storage: Файл не найден");
            }
        } else {
            System.out.println("local_storage: некорректный аргумент");
//            return;
        }
    }

    private static void sendNameFIleForDownloading(String inputLine) {

        if (isThereaSecondElement(inputLine)) {
            String command = getSecondElement(inputLine);
//            int i = Integer.parseInt(command);
//            client.sendByte(1);
            client.sendFileName(command);
//            client.sendByte("file".length())
//            ;
//            client.sendMessage("file");
//            client.sendMessage("text");
        } else {
            System.out.println("local_storage: некорректный управляющий байт");
//            return;
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

    private static String getSecondElement(String inputLine) {
        return inputLine.split(" ")[1];
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


    public static void sendMsg(String message) {
        client.sendMessage(message);
    }
}