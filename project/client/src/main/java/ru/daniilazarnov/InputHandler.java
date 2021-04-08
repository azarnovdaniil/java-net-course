package ru.daniilazarnov;

import helpers.ClientFileHelper;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.Scanner;

public class InputHandler implements Runnable {

    public static final String WRONG_REGISTRATION_TEXT = "Неверная команда регистрации";
    public static final String WRONG_UPLOAD_TEXT = "Неверная команда загрузки на сервер";
    public static final String WRONG_DOWNLOAD_TEXT = "Неверная команда загрузки с сервера";
    public static final String WRONG_RENAME_TEXT = "Неверная команда переименования файла";
    public static final String WRONG_REMOVE_TEXT = "Неверная команда удаления файла";
    public static final String WRONG_LIST_TEXT = "Неверная команда вывода списка файлов";
    private final InputStream stream;
    private final Client client;

    public InputHandler(Client client, InputStream stream) {
        this.client = client;
        this.stream = stream;
    }

    @Override
    public void run() {

        ClientFileHelper cfh = new ClientFileHelper(client.getSocket(), client.getUser());
        Scanner input = new Scanner(stream);

        while (!client.getSocket().isClosed()) {

            String textInput = input.nextLine();
            String[] command = textInput.split("\\s");

            //checkstyle
            final int registerLength = 4;
            final int uploadLength = 2;
            final int downloadLength = 2;
            final int renameLength = 3;
            final int removeLength = 2;
            final int listLength = 1;

            switch (command[0]) {

                case "register":
                    if (command.length == registerLength) {
                        client.register(command[1], command[2], command[registerLength - 1]); //fckng checkstyle!!!!!!
                    } else {
                        System.out.println(WRONG_REGISTRATION_TEXT);
                    }

                    break;

                case "auth":
                    client.auth(client.getConfig().getProperty("app.login"),
                            client.getConfig().getProperty("app.password"),
                            client.getConfig().getProperty("app.nickname"));
                    break;

                case "upload":
                    if (command.length == uploadLength) {
                        cfh.uploadFile(Path.of(command[1]));
                    } else {
                        System.out.println(WRONG_UPLOAD_TEXT);
                    }

                    break;

                case "download":
                    if (command.length == downloadLength) {
                        cfh.downloadFile(command[1]);
                    } else {
                        System.out.println(WRONG_DOWNLOAD_TEXT);
                    }

                    break;

                case "rename":
                    if (command.length == renameLength) {
                        cfh.renameFile(command[1], command[2]);
                    } else {
                        System.out.println(WRONG_RENAME_TEXT);
                    }

                    break;

                case "remove":
                    if (command.length == removeLength) {
                        cfh.removeFile(command[1]);
                    } else {
                        System.out.println(WRONG_REMOVE_TEXT);
                    }

                    break;

                case "list":
                    if (command.length == listLength) {
                        cfh.listFiles().forEach(System.out::println);
                    } else {
                        System.out.println(WRONG_LIST_TEXT);
                    }

                    break;

                case "exit":
                    break;

                default:
                    throw new IllegalStateException("Unexpected value: " + command[0]);
            }
        }
    }
}
