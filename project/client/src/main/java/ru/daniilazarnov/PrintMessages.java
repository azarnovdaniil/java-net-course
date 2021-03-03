package ru.daniilazarnov;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class PrintMessages {
    public static void invoke(String sourceText) {
        PrintMessages app = new PrintMessages();
        String fileName = sourceText;
        InputStream is = app.getFileFromResourceAsStream(fileName);
        printInputStream(is);
    }

    InputStream getFileFromResourceAsStream(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return inputStream;
        }

    }
    static void printInputStream(InputStream is) {

        try (InputStreamReader streamReader =
                     new InputStreamReader(is, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(streamReader)) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getUserName() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Please enter your name: ");
        String clientName=scanner.nextLine().trim();
        System.out.println("Welcome " + clientName + "!");
        return clientName;

    }
    public String getSecretKey() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please create (enter) your SECRET KEYWORD to identify your use of our cloud file service.");
        System.out.print("Please enter your Secret Key: ");
        String secretKey=scanner.nextLine();
        return secretKey;
    }

}


