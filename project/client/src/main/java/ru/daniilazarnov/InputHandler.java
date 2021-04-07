package ru.daniilazarnov;

import java.io.InputStream;
import java.util.Scanner;

public class InputHandler implements Runnable {

    InputStream stream;
    Client client;

    public InputHandler(Client client, InputStream stream) {
        this.client = client;
        this.stream = stream;
    }

    @Override
    public void run() {
        Scanner input = new Scanner(stream);

        String textInput = input.nextLine();
        String[] command = textInput.split("\\s");

        switch (command[0]){

            case "register": {
                if (command.length == 4) {
                    client.register(command[1], command[2], command[3]);
                } else {
                    System.out.println("Неверная команда регистрации");
                }
                break;
            }

            //todo доделать ввод команд
            case "auth": {
                client.auth(client.getConfig().getProperty("app.login"),
                        client.getConfig().getProperty("app.password"),
                        client.getConfig().getProperty("app.nickname"));
                break;
            }

            case "upload": {

                break;
            }

            case "download": {

                break;
            }

            case "rename": {

                break;
            }

            case "remove": {

                break;
            }

            case "list": {

                break;
            }

            case "exit": {

                break;
            }

            default:
                throw new IllegalStateException("Unexpected value: " + command[0]);
        }
    }
}
