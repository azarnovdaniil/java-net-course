package ru.daniilazarnov;
/**
 * Класс для регистрации/авторизации пользователя
 */

import java.util.Scanner;

import static ru.daniilazarnov.Client.*;

public class AuthorisationLevel {

    public static void invoke() {
        PrintMessages.invoke("MessageHello.txt");
        Scanner scanner = new Scanner(System.in);
        System.out.print("Please enter your name: ");

        clientName = scanner.nextLine();
        System.out.println("Welcome " + clientName + "!");

        System.out.print("Please create (enter) your SECRET KEYWORD to identify your use of our cloud file service: ");

        clientKEY = scanner.nextLine();
        System.out.println("Your SECRET KEYWORD is:" + clientKEY);

    }
}
