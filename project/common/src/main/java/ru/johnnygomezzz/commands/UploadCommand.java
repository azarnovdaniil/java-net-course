package ru.johnnygomezzz.commands;

import ru.johnnygomezzz.FileMessage;

public class UploadCommand {

    public void uploadCommand(FileMessage fm) {
        System.out.println("Файл " + fm.getFileName() + " успешно отправлен.");
    }
}
