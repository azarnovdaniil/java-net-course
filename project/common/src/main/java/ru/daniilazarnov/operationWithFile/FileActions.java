package ru.daniilazarnov.operationWithFile;

import ru.daniilazarnov.FileMsg;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class FileActions {
    public static void actionsWithFile(TypeOperation type, FileMsg fileMsg, String... paths) throws IOException {
        switch (type) {
            case CREATE_UPDATE:
                Files.write(Path.of(paths[0]), fileMsg.getBytes(), StandardOpenOption.CREATE);
                break;
            case COPY:
                Files.copy(Path.of(paths[0]), Path.of(paths[1]));
                break;
            case MOVE:
                Files.move(Path.of(paths[0]), Path.of(paths[1]));
                break;
            case DELETE:
                Files.delete(Path.of(paths[0]));
                break;
            case CREATE_DIR:
                Files.createDirectory(Path.of(paths[0]));
                break;
            case NEW:

        }
    }
}
