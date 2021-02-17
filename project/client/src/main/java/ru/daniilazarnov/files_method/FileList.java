package ru.daniilazarnov.files_method;

import io.netty.buffer.ByteBuf;
import org.apache.log4j.Logger;
import ru.daniilazarnov.ReceivingAndSendingStrings;
import ru.daniilazarnov.UtilMethod;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static ru.daniilazarnov.constants.Constants.DEFAULT_PATH_USER;
import static ru.daniilazarnov.string_method.StringMethod.getSecondElement;
import static ru.daniilazarnov.string_method.StringMethod.isThereaSecondElement;

public class FileList {
    private static final Logger LOG = Logger.getLogger(FileList.class);

    /**
     * Получает строку содержащую  файлы и каталоги по указанному пути
     *
     * @param inputLine введеная строка
     * @return результирующая строка имеет вид:  [папка] 'файл';
     */
    public static String getFilesList(String inputLine) {
        String result = "";
        String fileName;
        if (isThereaSecondElement(inputLine)) { // если после ls введено имя каталога получаем его
            fileName = getSecondElement(inputLine);
            if (!Files.isDirectory(Path.of(DEFAULT_PATH_USER, fileName))) {
                return "Файл не является каталогом";
            }
        } else {
            fileName = ""; // если имени каталога нет
        }
        try {
            result = UtilMethod.getFolderContents(fileName, "user");
        } catch (IOException e) {
            LOG.debug(e);
        }
        return result;
    }


    public static String getFilesListStringFromServer(ByteBuf buf) {
         return ReceivingAndSendingStrings.receiveAndEncodeString(buf);
    }



}
