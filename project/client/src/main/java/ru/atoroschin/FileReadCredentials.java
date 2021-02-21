package ru.atoroschin;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class FileReadCredentials implements ReadCredentials {
    private final String fileCredentials;

    public FileReadCredentials(String fileCredentials) {
        this.fileCredentials = fileCredentials;
    }

    @Override
    public Credentials read() throws IOException {
        Path file = Path.of(fileCredentials);
        if (Files.exists(file)) {
            Scanner scanner = new Scanner(new FileReader(file.toFile()));

            String readLine;
            System.out.print("Введите логин и пароль: ");
            if (scanner.hasNext()) {
                readLine = scanner.nextLine();
                String[] names = readLine.split("\\s", 2);
                return new Credentials(names[0], names[1]);
            }
        }
        return new Credentials();
    }

    @Override
    public void write(Credentials credentials) throws IOException {
        Path file = Path.of(fileCredentials);
        if (Files.exists(file)) {
            Files.createFile(file);
        }
        FileWriter writer = new FileWriter(file.toFile());
        writer.write(credentials.getLine());
    }
}
