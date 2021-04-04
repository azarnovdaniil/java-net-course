package ru.daniilazarnov;


import io.netty.handler.stream.ChunkedFile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;


public class ServerCommandReader {

    private final UserProfile profile;
    private static final Logger LOGGER = LogManager.getLogger(ServerCommandReader.class);

    ServerCommandReader(UserProfile profile) {

        this.profile = profile;
    }

    public void readMessage(ContextData messageContext) {
        LOGGER.info("Incoming message.");

        if (messageContext.getCommand() == CommandList.login.getNum()) {
            authorise(messageContext);

        } else if (messageContext.getCommand() == CommandList.register.getNum()) {
            register(messageContext);

        } else if (messageContext.getCommand() == CommandList.getFileList.getNum()) {
            sendFileList();

        } else if (messageContext.getCommand() == CommandList.download.getNum()) {
            sendFile(messageContext);

        } else if (messageContext.getCommand() == CommandList.delete.getNum()) {
            deleteFile(messageContext);

        } else if (messageContext.getCommand() == CommandList.fileUploadInfo.getNum()) {
            sendFileInfo(messageContext);

        } else if (messageContext.getCommand() == CommandList.rename.getNum()) {
            renameFile(messageContext);

        } else if (messageContext.getCommand() == CommandList.serverMessage.getNum()) {
            goOn();

        } else {
            profile.sendMessage("false%%%Command unknown.");
            LOGGER.info("Command unknown.");
        }
    }

    private void authorise(ContextData messageContext) {
        AuthorisationService.login(messageContext.getLogin(), messageContext.getPassword(), profile);
    }

    private void register(ContextData messageContext) {
        AuthorisationService.register(messageContext.getLogin(), messageContext.getPassword(), profile);
    }

    private void sendFileList() {
        LOGGER.info("File List request initiated.");
        File storage = Paths.get(this.profile.getAuthority()).toFile();
        File[] files = storage.listFiles();
        String fileList = Arrays.stream(files)
                .map((File::getName))
                .sorted()
                .collect(Collectors.joining("###"));

        profile.getContextData().setCommand(CommandList.getFileList.getNum());
        profile.getCurChannel().writeAndFlush(fileList.getBytes());
    }

    private synchronized void sendFile(ContextData messageContext) {
        LOGGER.info("File request initiated.");
        File toSend = Paths.get(profile.getAuthority(), messageContext.getFilePath()).toFile();
        if (toSend.exists()) {
            profile.getContextData().setCommand(CommandList.fileUploadInfo.getNum());
            profile.getContextData().setPassword(Objects.toString(toSend.length()));
            profile.getContextData().setFilePath(toSend.getName());
            profile.getCurChannel().writeAndFlush(new byte[1]);
            LOGGER.info("File info sent.");

            try {

                wait();

                LOGGER.info("Sending file "+toSend.getName() + " size "+toSend.length());
                profile.getContextData().setCommand(CommandList.upload.getNum());
                ChunkedFile chunk = new ChunkedFile(toSend, 1024);
                profile.getCurChannel().writeAndFlush(chunk);

                wait();

                chunk.close();

            } catch (Exception e) {
                LOGGER.error("SWW waiting for respond!!", LOGGER.throwing(e));
            }
        } else {
            profile.sendMessage("false%%%File not found.");
            LOGGER.info("File not found.");
        }
    }

    private void deleteFile(ContextData messageContext) {
        LOGGER.info("File delete request.");
        File toDelete = Paths.get(profile.getAuthority(), messageContext.getFilePath()).toFile();
        if (toDelete.exists()) {
            try {
                Files.delete(toDelete.toPath());
                profile.sendMessage("true%%%File " + messageContext.getFilePath() + " deleted successfully.");
                LOGGER.info("File "+ messageContext.getFilePath() +" deleted.");
            } catch (IOException e) {
                profile.sendMessage("false%%%Error occurred. File delete failed.");
                LOGGER.error("SWW deleting a file", LOGGER.throwing(e));
            }
        } else {
            profile.sendMessage("false%%%File " + messageContext.getFilePath() + " not found.");
            LOGGER.info("File not found.");
        }
    }

    private void sendFileInfo(ContextData messageContext) {
        File toDelete = Paths.get(profile.getAuthority(), messageContext.getFilePath()).toFile();
        if (toDelete.exists()) {
            profile.sendMessage("YesNoTrue&&&Too gard for me!");
            try {
                Files.delete(toDelete.toPath());
            } catch (IOException e) {
                LOGGER.error("SWW deleting a file", LOGGER.throwing(e));
            }
        } else profile.sendMessage("YesNoFalse&&&Too good...");
        profile.setFileLength(Long.parseLong(messageContext.getPassword()));
        profile.sendMessage("SYSTEM");
    }

    private synchronized void goOn() {
        this.notify();
    }

    private void renameFile(ContextData messageContext) {
        LOGGER.info("Rename request initiated.");
        File toRename = Paths.get(profile.getAuthority(), messageContext.getFilePath()).toFile();
        File newName = Paths.get(profile.getAuthority(), messageContext.getLogin()).toFile();
        if (newName.exists()) {
            profile.sendMessage("false%%%Rename failed! File " + messageContext.getLogin() + " already exists!");
            return;
        }
        if (toRename.exists()) {
            if (toRename.renameTo(newName)) {
                profile.sendMessage("true%%%File " + messageContext.getFilePath() + " successfully renamed to " + messageContext.getLogin());
                LOGGER.info("File "+messageContext.getFilePath()+" renamed to "+messageContext.getLogin());
            } else profile.sendMessage("File rename failed!");
        } else {
            profile.sendMessage("false%%%File " + messageContext.getFilePath() + " not found.");
        }
    }

}
