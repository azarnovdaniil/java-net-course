package ru.daniilazarnov;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class PrintMessages {
    public static void invoke(String sourceText) {
        PrintMessages app = new PrintMessages();
        String fileName = sourceText;
        InputStream is = app.getFileFromResourceAsStream(fileName);
        printInputStream(is);
    }

    private InputStream getFileFromResourceAsStream(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return inputStream;
        }

    }
    private static void printInputStream(InputStream is) {

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


