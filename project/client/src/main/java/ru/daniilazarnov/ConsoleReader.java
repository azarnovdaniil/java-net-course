package ru.daniilazarnov;
/**
 * Класс для получения команнд пользователя
 * */
import java.util.Scanner;

public class ConsoleReader {
    public static Scanner invoke() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter your name: ");
        if (scanner.hasNext()) {
            Client.clientName = scanner.nextLine();
            System.out.println("Welcome " + Client.clientName);
        }
        System.out.println("Please create (enter) your SECRET KEYWORD to identify your use of our cloud file service: ");
        if (scanner.hasNext()) {
            Client.clientKEY = scanner.nextLine();
            System.out.println("Your SECRET KEYWORD is:" + Client.clientKEY);
        }
        return scanner;
    }
}
