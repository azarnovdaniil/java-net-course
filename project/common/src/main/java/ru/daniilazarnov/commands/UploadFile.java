package ru.daniilazarnov.commands;

import ru.daniilazarnov.MessagePacket;



public final class UploadFile extends Commands {

    @Override
    public String getMessageForInput() {
        return messageForInput;
    }

    public UploadFile(String s) {
        this.messageForInput = s;

    }

    @Override
    public MessagePacket runCommands(MessagePacket messagePacket) {
//        System.out.println("Загрузить файл\"" + fileName + "\" в директорию: " + userDir);
//
//        if (!Files.exists(userDir)) {
//            try {
//                Files.createDirectories(userDir);
//
//                System.out.println("Directory is created!");
//
//            } catch (IOException e) {
//
//                System.err.println("Failed to create directory!" + e.getMessage());
//
//            }
//        }
//        Path newFilePath = null;
//        try {
//            newFilePath = Files.createFile(userDir);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        System.out.println("New file created: " + newFilePath);
        return messagePacket;
    }

    @Override
    public MessagePacket runClientCommands(MessagePacket messagePacket) {
        return null;
    }
}
