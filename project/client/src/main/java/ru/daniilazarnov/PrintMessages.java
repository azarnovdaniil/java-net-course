package ru.daniilazarnov;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class PrintMessages {
    ClassLoader classLoader;
    InputStream is;

    public void printFile(String fileName) {
        this.classLoader = getClass().getClassLoader();
        this.is = classLoader.getResourceAsStream(fileName);
        if (is != null) {
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


    }

    public String getUserName() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Please enter your name: ");
        String clientName = scanner.nextLine().trim();
        System.out.println("Welcome " + clientName + "!");
        return clientName;

    }

    public String getSecretKey() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please create (enter) your SECRET KEYWORD to identify your use of our cloud file service.");
        System.out.print("Please enter your Secret Key: ");
        return scanner.nextLine();
    }

}


