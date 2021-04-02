package ru.daniilazarnov;


import io.netty.handler.stream.ChunkedFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;


public class ServerCommandReader extends CommandReader {

    private final UserProfile profile;

    ServerCommandReader(ContextData messageContext, UserProfile profile) {
        super(messageContext);
        this.profile = profile;
    }


    @Override
    public void run() {

        if (super.messageContext.getCommand() == CommandList.login.getNum()) {
            AuthorisationService.login(super.messageContext.getLogin(), super.messageContext.getPassword(), profile);

        } else if (super.messageContext.getCommand() == CommandList.register.getNum()) {
            AuthorisationService.register(super.messageContext.getLogin(), super.messageContext.getPassword(), profile);
        } else if (super.messageContext.getCommand() == CommandList.getFileList.getNum()) {
            File storage = Paths.get(profile.getAuthority()).toFile();
            File[] files = storage.listFiles();
            String fileList = Arrays.stream(files)
                    .map((file -> file.getName()))
                    .sorted()
                    .collect(Collectors.joining("###"));

            profile.getContextData().setCommand(CommandList.getFileList.getNum());
            profile.getCurChannel().writeAndFlush(fileList.getBytes());

        } else if (super.messageContext.getCommand() == CommandList.download.getNum()) {
            File toSend = Paths.get(profile.getAuthority() + "\\" + super.messageContext.getFilePath()).toFile();
            if (toSend.exists()) {
                profile.getContextData().setCommand(CommandList.fileUploadInfo.getNum());
                profile.getContextData().setPassword(Objects.toString(toSend.length()));
                profile.getContextData().setFilePath(toSend.getName());
                profile.getCurChannel().writeAndFlush(new byte[1]);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                profile.getContextData().setCommand(CommandList.upload.getNum());
                try {

                    ChunkedFile chunk = new ChunkedFile(toSend, 1024);
                    profile.getCurChannel().writeAndFlush(chunk);

                    Thread.sleep(5000);
                    System.out.println("end");
                    chunk.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else profile.sendMessage("false%%%File not found.");

        } else if (super.messageContext.getCommand() == CommandList.delete.getNum()) {
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

        } else if (super.messageContext.getCommand() == CommandList.fileUploadInfo.getNum()) {
            File toDelete = Paths.get(profile.getAuthority() + "\\" + super.messageContext.getFilePath()).toFile();
            if (toDelete.exists()) {
                profile.sendMessage("YesNoTrue&&&Too gard for me!");
                try {
                    Files.delete(toDelete.toPath());
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException("SWW deleting a file");
                }
            }else profile.sendMessage("YesNoFalse&&&Too good...");
            profile.setFileLength(Long.parseLong(super.messageContext.getPassword()));
            profile.sendMessage("SYSTEM");
        } else {
            profile.sendMessage("false%%%Command unknown.");
        }
    }

}
