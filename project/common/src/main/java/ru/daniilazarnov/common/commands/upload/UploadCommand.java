package ru.daniilazarnov.common.commands.upload;


import io.netty.channel.*;
import ru.daniilazarnov.common.commands.Command;
import ru.daniilazarnov.common.files.FileSender;
import java.io.IOException;
import java.nio.file.Paths;

public class UploadCommand implements Command {
    private Channel channel;
    private String stringPath;

    public UploadCommand(Channel channel, String stringPath) {
        this.channel = channel;
        this.stringPath = stringPath;
    }

    @Override
    public void execute() {
        try {
            FileSender fileSender = new FileSender(channel);
            fileSender.sendFile(Paths.get(stringPath),
                    future -> {
                        if (!future.isSuccess()) {
                            future.cause().printStackTrace();
                        }
                        if (future.isSuccess()) {
                            System.out.println("Upload command has been sent");
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
