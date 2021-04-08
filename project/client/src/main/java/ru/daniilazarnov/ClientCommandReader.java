package ru.daniilazarnov;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

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
    private final Consumer<String> systemMessage;
    private static final Logger LOGGER = LogManager.getLogger(ClientCommandReader.class);
    static final Marker TO_CON = MarkerManager.getMarker("CONS");

    /**
     * Analyses incoming commands from the server and executes them.
     *
     * @param hub            - App class manager to work with.
     * @param messageContext - context info for incoming command.
     * @param pathHolder     - tools and info storage.
     * @param goOn           - inits next step for step-divided processes.
     * @param systemMessage  - respond to the server, if it is needed.
     */


    ClientCommandReader(ClientModuleManager hub, ContextData messageContext, ClientPathHolder pathHolder,
                        Consumer<Boolean> goOn, Consumer<String> systemMessage) {
        super(messageContext);
        this.hub = hub;
        this.pathHolder = pathHolder;
        this.goOn = goOn;
        this.systemMessage = systemMessage;
    }


    @Override
    public void run() {

        if (super.messageContext.getCommand() == CommandList.login.getNum()
                || super.messageContext.getCommand() == CommandList.register.getNum()) {
            isDoneAuth(readServerMessage());

        } else if (super.messageContext.getCommand() == CommandList.getFileList.getNum()) {
            printFileList(new String(super.messageContext.getContainer()));

        } else if (super.messageContext.getCommand() == CommandList.serverMessage.getNum()) {
            readServerMessage();
        } else if (super.messageContext.getCommand() == CommandList.fileUploadInfo.getNum()) {
            fileUpload();
        } else {
            hub.console.print("Command unknown");
            LOGGER.info("Unknown command arrived: " + super.messageContext.getCommand());
        }

    }


    private String readServerMessage() {
        String message = new String(super.messageContext.getContainer());
        LOGGER.info("New message from server arrived: " + message);
        if (message.equals("SYSTEM")) {
            goOn.accept(true);
            return message;
        } else if (message.startsWith("YesNoTrue")) {
            hub.addThread(() -> new YesNoConsole(
                    s -> hub.inAnalyser.yesNoAnswer(s),
                    "The file already exists in your storage. Rewrite? (yes/no)").run());
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
        LOGGER.info("File list arrived.");
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
        LOGGER.info("Incoming file: " + super.messageContext.getFilePath() + " size "
                + super.messageContext.getPassword());
        pathHolder.setFileLength(Long.parseLong(super.messageContext.getPassword()));
        File incoming = Paths.get(hub.getPathToRepo(), super.messageContext.getFilePath()).toFile();
        if (incoming.exists()) {
            try {
                Files.delete(incoming.toPath());
            } catch (IOException e) {
                LOGGER.error(TO_CON, "SWW deleting a file.", LOGGER.throwing(e));
            }
        }
        this.systemMessage.accept("RESPOND");
    }
}

