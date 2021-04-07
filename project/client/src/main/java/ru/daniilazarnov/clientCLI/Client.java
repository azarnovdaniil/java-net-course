package ru.daniilazarnov.clientCLI;

import ru.daniilazarnov.clientConnection.Connection;
import ru.daniilazarnov.clientConnection.IClientConnection;
import java.util.Scanner;

public class Client {
    private static IClientConnection connection;
    public static void main(String[] args) {
        connection = new Connection();
        connection.connect();
        String message = "";
        do {
            Scanner scanner = new Scanner(System.in);
            message = scanner.nextLine();
            CommandInterpretation.interpreter(message, connection);

        } while (!message.equals("disconnect"));
            connection.disconnect();
    }
}
