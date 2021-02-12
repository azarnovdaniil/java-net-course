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
    private static final Logger logger = Logger.getLogger(ClientHandler.class.getName());

    private ObjectEncoderOutputStream objectOut;
    private String clientLogin;

    public ClientHandler(ObjectEncoderOutputStream objectOut) {
        this.objectOut = objectOut;
    }

    public void chooseCommand(String msg) throws IOException {
        String[] msgParts = msg.split("\\s");

        if (msg.startsWith("upload")) {
            logger.info("Upload started...");

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

                    logger.debug("Uploading started");
                    for (int i = 0; i < partsCount; i++) {
                        int readedBytes = in.read(fm.getData());
                        fm.setPartNumber(i + 1);
                        if (readedBytes < bufSize) {
                            fm.setData(Arrays.copyOfRange(fm.getData(), 0, readedBytes));
                        }
                        objectOut.writeObject(fm);
                        objectOut.flush();
                        logger.debug("Part: " + (i + 1) + " was sent");
                    }
                    in.close();
                    logger.debug("File was fully uploaded");
                }
            } catch (IOException e) {
                logger.error("SWW while uploading file");
                throw new RuntimeException("SWW", e);
            }
        }
        else if (msg.startsWith("download")) {
            logger.info("Download started...");
            RequestMessage rm = new RequestMessage(msgParts[0], msgParts[1], clientLogin);
            objectOut.writeObject(rm);
            objectOut.flush();
        }
        else if (msg.startsWith("list")) {
            logger.info("Waiting the list from server");
            RequestMessage rm = new RequestMessage(msgParts[0], clientLogin);
            objectOut.writeObject(rm);
            objectOut.flush();
        }
        else if (msg.startsWith("remove")) {
            logger.info("F, file was removed");
            RequestMessage rm = new RequestMessage(msgParts[0], msgParts[1], clientLogin);
            objectOut.writeObject(rm);
            objectOut.flush();
        }
        else if (msg.startsWith("rename")) {
            logger.info("Name was changed");
            RequestMessage rm = new RequestMessage(msgParts[0], msgParts[1], msgParts[2], clientLogin);
            objectOut.writeObject(rm);
            objectOut.flush();
        }
        else if (msg.startsWith("auth")) {
            logger.info("Waiting for auth server");
            DBMessage dbm = new DBMessage(msgParts[0], msgParts[1], msgParts[2]);
            objectOut.writeObject(dbm);
            objectOut.flush();
        }
        else if (msg.startsWith("reg")) {
            logger.info("Stand by for registration");
            DBMessage dbm = new DBMessage(msgParts[0], msgParts[1], msgParts[2]);
            objectOut.writeObject(dbm);
            objectOut.flush();
        }
        else logger.info("Unknown command");
    }

    public void setClientLogin(String clientLogin) {
        this.clientLogin = clientLogin;
    }
}
