package ru.sviridovaleksey.workwithfiles;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class WorkWithFile {


    public void createNewFile (String userName, String fileName, String cellDirectory) {

        if (!Files.isDirectory(Path.of("project/server/"+userName))) {
            System.out.println("Папки пользователя не существует, зарегестрируйтесь"); return;
        }
        if(!Files.isDirectory(Path.of("project/server/"+userName+"/"+cellDirectory))) {
            System.out.println("Целевой папки не найдено");   return;
        }
        try {
                while (!Files.exists(Path.of("project/server/" + fileName))) {
                Files.createFile(Paths.get("project/server/" + fileName)); }

        } catch (IOException e) {
                System.out.println(e.getMessage() + "Не удалось создать файл");
        }
    }

    public void createNewDirectory (String userName, String directoryName) {
        if (Files.isDirectory(Path.of("project/server/"+userName+"/"+directoryName))) {
            System.out.println("Такая директория уже существует");
        } else {
            try {
                while (!Files.exists(Path.of("project/server/"+userName))) {
                Files.createDirectory(Path.of("project/server/"+userName)); }
            } catch (IOException e) {
                System.out.println(e.getMessage() + "Не удалось создать директорию");
            }
        }
    }




}
