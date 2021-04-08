package ru.daniilazarnov;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class CommandHandler {
    Scanner scanner = new Scanner(System.in);
    NetworkService networkService;
    String userCom;

    public CommandHandler(NetworkService networkService) {
        this.networkService = networkService;
    }

    public void sendCommand() throws IOException {
        System.out.println(new String(Files.readAllBytes(Paths.get(
                "C:\\Users\\polit\\IdeaProjects\\java-net-course\\project\\" +
                        "client\\src\\main\\java\\ru\\daniilazarnov\\Description.txt"))));

        while (true) {
            userCom = scanner.nextLine();

            if (userCom.startsWith("~cf")) {
                String[] parts = userCom.split(" ");
                String name = parts[1];
                networkService.sendCommand(Command.commandCreateFile(name));
            }
            else if (userCom.startsWith("~cd")) {
                String[] parts = userCom.split(" ");
                String name = parts[1];
                networkService.sendCommand(Command.commandCreateDir(name));
            }
            else if (userCom.startsWith("~r")) { // если здесь передаём 2 а не три слова есть ошибка. Нужно редактировать
                String[] parts = userCom.split(" ");
                String name = parts[1];
                String newName = parts[2];
                networkService.sendCommand(Command.commandRename(name, newName));
            }
            else if (userCom.startsWith("~d")) {
                String[] parts = userCom.split(" ");
                String name = parts[1];
                networkService.sendCommand(Command.commandDelete(name));
            }
            else if (userCom.equals("/end")) {
                scanner.close();
                break;
            }
            else {
                System.out.println("Wrong command");
            }
        }
    }
}