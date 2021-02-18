package ru.daniilazarnov.files_method;

import ru.daniilazarnov.util.UtilMethod;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static ru.daniilazarnov.string_method.StringMethod.getSecondElement;
import static ru.daniilazarnov.string_method.StringMethod.isThereaSecondElement;
import static ru.daniilazarnov.constants.Constants.*;

public class DeleteFile {

    public static String deleteFile(String inputLine) {
        String result = "";
        String fileName;
        if (isThereaSecondElement(inputLine)) { // если после ls введено имя каталога получаем его
            fileName = getSecondElement(inputLine);
            Path path = Paths.get(DEFAULT_PATH_USER + File.separator, fileName);
            if (Files.exists(path)) {
                UtilMethod.deleteFile(path.toString());
                if (Files.exists(path)) {
                    return "Не удалось удалить указанный файл";
                } else {
                    return "Файл успешно удален";
                }
            } else {
                return "Неправильное имя файла";
            }
        }
        return result;
    }
}
