package ru.daniilazarnov.clientCLI;

import ru.daniilazarnov.clientConnection.IClientConnection;

import java.nio.file.Paths;
import java.util.Arrays;

public class CommandInterpretation {
    private static final String DEFAULT_PASSWORD = null;

    public static void interpreter(String message, IClientConnection connection) {
        String[] splitMessage = message.split(" ");
        String command = splitMessage[0];
        String[] arguments = Arrays.copyOfRange(splitMessage, 1, splitMessage.length);
        switch (command) {
            case "mkd":
                if (CheckCommand.isMakeDir(arguments)) {
                    connection.makeDirectory(arguments[0]);
                }
                break;
            case "cd":
                if (CheckCommand.isChangeDir(arguments)) {
                    connection.changeDirectory(Paths.get(arguments[0]));
                }
                break;
            case "retr":
                if (CheckCommand.isDownloadFile(arguments)) {
                    connection.downloadFile(arguments[0]);
                }
                break;
            case "user":
                if (CheckCommand.isAuth(arguments)) {
                    connection.authorization(arguments[0], DEFAULT_PASSWORD);
                }
                break;
            case "connect":
                if (CheckCommand.isConnect(arguments)) {
                    connection.connect();
                }
                break;
            case "pwd":
                if (CheckCommand.isPresentDir(arguments)) {
                    connection.presentWorkDirectory();
                }
                break;
            case "disconnect":
                return;
            case "ls":
                if (CheckCommand.isListOfFile(arguments)) {
                    connection.listOfFileDirectory();
                }
                break;
            case "help":
                if (CheckCommand.isShowHelp(arguments)) {
                    connection.showHelp();
                }
            case "stor":
                if (CheckCommand.isUlpoadFile(arguments)) {
                    connection.uploadFile(Paths.get(arguments[0]));
                }
                break;
            default:
                System.out.println("Unknown command\n>");
        }
    }
}
