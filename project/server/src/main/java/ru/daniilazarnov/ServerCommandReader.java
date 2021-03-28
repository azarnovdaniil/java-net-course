package ru.daniilazarnov;


import io.netty.handler.stream.ChunkedFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;


public class ServerCommandReader extends CommandReader {

    private UserProfile profile;

    ServerCommandReader(ContextData messageContext, UserProfile profile) {
        super(messageContext);
        this.profile = profile;
    }


    @Override
    public void run() {

        if (super.messageContext.getCommand() == CommandList.login.ordinal()) {
            AuthorisationService.login(super.messageContext.getLogin(), super.messageContext.getPassword(), profile);

        } else if (super.messageContext.getCommand() == CommandList.register.ordinal()) {
            AuthorisationService.register(super.messageContext.getLogin(), super.messageContext.getPassword(), profile);
        } else if (super.messageContext.getCommand() == CommandList.getFileList.ordinal()) {
            File storage = Paths.get(profile.getAuthority()).toFile();
            File[] files = storage.listFiles();
            String fileList = Arrays.stream(files)
                    .map((file -> file.getName()))
                    .sorted()
                    .collect(Collectors.joining("###"));

            profile.getContextData().setCommand(CommandList.getFileList.ordinal());
            profile.getCurChannel().writeAndFlush(fileList.getBytes());

        } else if (super.messageContext.getCommand() == CommandList.download.ordinal()) {
            File toSend = Paths.get(profile.getAuthority()+"\\"+super.messageContext.getFilePath()).toFile();
            if(toSend.exists()){
                profile.getContextData().setCommand(CommandList.fileUploadInfo.ordinal());
                profile.getContextData().setPassword(Objects.toString(toSend.length()));
                profile.getContextData().setFilePath(toSend.getName());
                profile.getCurChannel().writeAndFlush(new byte[1]);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                profile.getContextData().setCommand(CommandList.upload.ordinal());
                try {
                    profile.getCurChannel().writeAndFlush(new ChunkedFile(toSend, 1024));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }else profile.sendMessage("false%%%File not found.");

        } else if (super.messageContext.getCommand() == CommandList.delete.ordinal()) {
            File toDelete = Paths.get(profile.getAuthority() + super.messageContext.getFilePath()).toFile();
            if (toDelete.exists()) {
                try {
                    Files.delete(toDelete.toPath());
                    profile.sendMessage("true%%%File " + messageContext.getFilePath() + " deleted successfully.");
                } catch (IOException e) {
                    e.printStackTrace();
                    profile.sendMessage("false%%%Error occurred. File delete failed.");
                    throw new RuntimeException("SWW deleting a file");
                }
            } else {
                profile.sendMessage("false%%%File " + messageContext.getFilePath() + " not found.");
            }

        } else if (super.messageContext.getCommand() == CommandList.fileUploadInfo.ordinal()) {
            File toDelete = Paths.get(profile.getAuthority() +"\\"+ super.messageContext.getFilePath()).toFile();
            if (toDelete.exists()) {
                try {
                    Files.delete(toDelete.toPath());
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException("SWW deleting a file");
                }
                profile.setFileLength(Long.parseLong(super.messageContext.getPassword()));
            } else {
                profile.sendMessage("Command unknown.");
            }
        }

    }

}
