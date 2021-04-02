package ru.daniilazarnov;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.function.Consumer;

public class ClientCommandReader extends CommandReader {
    private final ClientModuleManager hub;
    private final ClientPathHolder pathHolder;
    private final Consumer<Boolean> goOn;


    ClientCommandReader(ClientModuleManager hub, ContextData messageContext, ClientPathHolder pathHolder, Consumer<Boolean> goOn) {
        super(messageContext);
        this.hub = hub;
        this.pathHolder = pathHolder;
        this.goOn = goOn;
    }


    @Override
    public void run() {

        if (super.messageContext.getCommand() == CommandList.login.getNum() ||
                super.messageContext.getCommand() == CommandList.register.getNum()) {
            isDoneAuth(readServerMessage());

        } else if (super.messageContext.getCommand() == CommandList.getFileList.getNum()) {
            printFileList(new String(super.messageContext.getContainer()));

        } else if (super.messageContext.getCommand() == CommandList.serverMessage.getNum()) {
            readServerMessage();
        } else if (super.messageContext.getCommand() == CommandList.fileUploadInfo.getNum()) {
            fileUpload();
        } else {
            hub.console.print("Command unknown");
        }

    }


    private String readServerMessage() {
        String message = new String(super.messageContext.getContainer());
        if (message.equals("SYSTEM")) {
            goOn.accept(true);
            return message;
        } else if (message.startsWith("YesNoTrue")) {
            hub.addThread(() -> new YesNoConsole(
                    s -> hub.inAnalyser.yesNoAnswer(s), "The file already exists in your storage. Rewrite? (yes/no)").run());
            return message;
        } else if (message.startsWith("YesNoFalse")) {
            hub.addThread(() -> hub.inAnalyser.contin());
            return message;
        } else {
            String[] mesSet = message.split("%%%");
            hub.console.print(mesSet[1]);
            return mesSet[0];
        }
    }

    private void isDoneAuth(String result) {
        if (result.equals("false")) {
            hub.setAuthorised(false);
        } else if (result.equals("true")) {
            hub.setAuthorised(true);
        }
    }

    private void printFileList(String fileList) {
        if (fileList.isEmpty()) {
            hub.console.print("Your storage is empty.");
            return;
        }
        String[] files = fileList.split("###");
        System.out.println("Your cloud repo contains:");
        Arrays.stream(files).forEach(System.out::println);
        hub.console.print("************************");
    }

    private void fileUpload() {
        pathHolder.setFileLength(Long.parseLong(super.messageContext.getPassword()));
        File incoming = Paths.get(hub.getPathToRepo(), super.messageContext.getFilePath()).toFile();
        if (incoming.exists()) {
            try {
                Files.delete(incoming.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

