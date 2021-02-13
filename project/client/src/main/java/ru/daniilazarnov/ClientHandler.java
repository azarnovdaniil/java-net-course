package ru.daniilazarnov;

import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class ClientHandler {
    private static final Logger LOGGER = Logger.getLogger(ClientHandler.class.getName());

    private ObjectEncoderOutputStream objectOut;
    private String clientLogin;

    public ClientHandler(ObjectEncoderOutputStream objectOut) {
        this.objectOut = objectOut;
    }

    public void chooseCommand(String msg) throws IOException {
        String[] msgParts = msg.split("\\s");

        if (msg.startsWith("upload")) {
            LOGGER.info("Upload started...");

            try {
                if (Files.exists(Paths.get("./project/clients_vault/" + clientLogin + "/" + msgParts[1]))) {
                    File file = new File("./project/clients_vault/" + clientLogin + "/" + msgParts[1]);
                    int bufSize = 1024 * 1024 * 10;
                    int partsCount = (int) (file.length() / bufSize);
                    if (file.length() % bufSize != 0) {
                        partsCount++;
                    }

                    FileMessage fm = new FileMessage(file.getName(), -1, partsCount, new byte[bufSize], clientLogin);
                    FileInputStream in = new FileInputStream(file);

                    LOGGER.debug("Uploading started");
                    for (int i = 0; i < partsCount; i++) {
                        int readedBytes = in.read(fm.getData());
                        fm.setPartNumber(i + 1);
                        if (readedBytes < bufSize) {
                            fm.setData(Arrays.copyOfRange(fm.getData(), 0, readedBytes));
                        }
                        objectOut.writeObject(fm);
                        objectOut.flush();
                        LOGGER.debug("Part: " + (i + 1) + " was sent");
                    }
                    in.close();
                    LOGGER.debug("File was fully uploaded");
                }
            } catch (IOException e) {
                LOGGER.error("SWW while uploading file");
                throw new RuntimeException("SWW", e);
            }
        }
        else if (msg.startsWith("download")) {
            LOGGER.info("Download started...");
            RequestMessage rm = new RequestMessage(msgParts[0], msgParts[1], clientLogin);
            objectOut.writeObject(rm);
            objectOut.flush();
        }
        else if (msg.startsWith("list")) {
            LOGGER.info("Waiting the list from server");
            RequestMessage rm = new RequestMessage(msgParts[0], clientLogin);
            objectOut.writeObject(rm);
            objectOut.flush();
        }
        else if (msg.startsWith("remove")) {
            LOGGER.info("F, file was removed");
            RequestMessage rm = new RequestMessage(msgParts[0], msgParts[1], clientLogin);
            objectOut.writeObject(rm);
            objectOut.flush();
        }
        else if (msg.startsWith("rename")) {
            LOGGER.info("Name was changed");
            RequestMessage rm = new RequestMessage(msgParts[0], msgParts[1], msgParts[2], clientLogin);
            objectOut.writeObject(rm);
            objectOut.flush();
        }
        else if (msg.startsWith("auth")) {
            LOGGER.info("Waiting for auth server");
            DBMessage dbm = new DBMessage(msgParts[0], msgParts[1], msgParts[2]);
            objectOut.writeObject(dbm);
            objectOut.flush();
        }
        else if (msg.startsWith("reg")) {
            LOGGER.info("Stand by for registration");
            DBMessage dbm = new DBMessage(msgParts[0], msgParts[1], msgParts[2]);
            objectOut.writeObject(dbm);
            objectOut.flush();
        }
        else if (msg.startsWith("help")) {
            helpInfo();
        }
        else LOGGER.info("Unknown command");
    }

    public void setClientLogin(String clientLogin) {
        this.clientLogin = clientLogin;
    }

    private static void helpInfo() {
        LOGGER.info("Greetings, client");
        LOGGER.info("Here is list of actual commands with samples: ");
        LOGGER.info("auth - authorize before using your storage (auth user1 1)");
        LOGGER.info("reg - if you didn't use our net storage before, create your account now! (reg user10 10)");
        LOGGER.info("upload - uploads files from your personal directory to server (upload 1.txt)");
        LOGGER.info("download - downloads file from remote server storage to your directory (download 2.png)");
        LOGGER.info("list - shows you files at your remote storage at server (list)");
        LOGGER.info("remove - delete file from server (remove ai.zip)");
        LOGGER.info("rename - change name of your file at storage (rename ai.zip qwerty.zip)");
        LOGGER.info("exit - closes application");
    }
}
