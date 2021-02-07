package ru.daniilazarnov;

import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;

public class ClientHandler {

    private ObjectEncoderOutputStream objectOut;
    private ObjectDecoderInputStream objectIn;

    public ClientHandler(ObjectEncoderOutputStream objectOut, ObjectDecoderInputStream objectIn) {
        this.objectOut = objectOut;
        this.objectIn = objectIn;
    }

    public void chooseCommand(String msg) throws IOException {
        String[] msgParts = msg.split("\\s");

        if (msg.startsWith("upload")) {
            System.out.println("Upload started...");
            System.out.println(msgParts[1]);
            if (Files.exists(Paths.get("./project/client/src/main/java/ru/daniilazarnov/client_vault/" + msgParts[1]))) {
                FileMessage fm = new FileMessage(Paths.get("./project/client/src/main/java/ru/daniilazarnov/client_vault/" + msgParts[1]));
                objectOut.writeObject(fm);
                objectOut.flush();
            }
        }
        else if (msg.startsWith("download")) {
            System.out.println("Download started...");
            RequestMessage rm = new RequestMessage(msgParts[0], msgParts[1]);
            objectOut.writeObject(rm);
            objectOut.flush();
        }
        else if (msg.startsWith("show")) {
            System.out.println("Your files: ");
            RequestMessage rm = new RequestMessage(msgParts[0]);
            objectOut.writeObject(rm);
            objectOut.flush();
        }
        else if (msg.startsWith("remove")) {
            System.out.println("F");
            RequestMessage rm = new RequestMessage(msgParts[0], msgParts[1]);
            objectOut.writeObject(rm);
            objectOut.flush();
        }
        else if (msg.startsWith("rename")) {
            System.out.println("Name was changed");
            RequestMessage rm = new RequestMessage(msgParts[0], msgParts[1], msgParts[2]);
            objectOut.writeObject(rm);
            objectOut.flush();
        }
        else System.out.println("Unknown command");
    }

}
