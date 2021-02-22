package ru.daniilazarnov;


import java.util.Scanner;

public class Client {



    public static void main(String[] args) {

        new ClientServerConnection();

        System.out.println("Client connected... ");
        System.out.println("Navigation: ");
        System.out.println("Please, enter your username:");

        Scanner scanner = new Scanner(System.in);
        String clientLogin = scanner.nextLine ();

    }


}
