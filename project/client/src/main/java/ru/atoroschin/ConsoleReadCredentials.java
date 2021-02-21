package ru.atoroschin;

import java.util.Scanner;

public class ConsoleReadCredentials implements ReadCredentials {
    @Override
    public Credentials read() {
        Scanner scanner = new Scanner(System.in);
        String readLine;
        System.out.print("Введите логин и пароль: ");
        if (scanner.hasNext()) {
            readLine = scanner.nextLine();
            String[] names = readLine.split("\\s", 2);
            return new Credentials(names[0], names[1]);
        }
        return new Credentials();
    }

    @Override
    public void write(Credentials credentials) {

    }
}
