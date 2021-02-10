package ru.daniilazarnov;

import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ClientHandler {

    private ObjectEncoderOutputStream objectOut;
    private String clientLogin;

    public ClientHandler(ObjectEncoderOutputStream objectOut) {
        this.objectOut = objectOut;
    }

    public void chooseCommand(String msg) throws IOException {
        String[] msgParts = msg.split("\\s");

        if (msg.startsWith("upload")) {
            System.out.println("Upload started...");
            if (Files.exists(Paths.get("./project/client/src/main/java/ru/daniilazarnov/client_vault/" + clientLogin + "/" + msgParts[1]))) {
                FileMessage fm = new FileMessage(Paths.get("./project/client/src/main/java/ru/daniilazarnov/client_vault/" + clientLogin + "/" + msgParts[1]), clientLogin);
                objectOut.writeObject(fm);
                objectOut.flush();
            }
        }
        else if (msg.startsWith("download")) {
            System.out.println("Download started...");
            RequestMessage rm = new RequestMessage(msgParts[0], msgParts[1], clientLogin);
            objectOut.writeObject(rm);
            objectOut.flush();
        }
        else if (msg.startsWith("list")) {
            System.out.println("Your files: ");
            RequestMessage rm = new RequestMessage(msgParts[0], clientLogin);
            objectOut.writeObject(rm);
            objectOut.flush();
        }
        else if (msg.startsWith("remove")) {
            System.out.println("F");
            RequestMessage rm = new RequestMessage(msgParts[0], msgParts[1], clientLogin);
            objectOut.writeObject(rm);
            objectOut.flush();
        }
        else if (msg.startsWith("rename")) {
            System.out.println("Name was changed");
            RequestMessage rm = new RequestMessage(msgParts[0], msgParts[1], msgParts[2], clientLogin);
            objectOut.writeObject(rm);
            objectOut.flush();
        }
        else if (msg.startsWith("auth")) {
            System.out.println("Waiting for auth server");
            DBMessage dbm = new DBMessage(msgParts[0], msgParts[1], msgParts[2]);
            objectOut.writeObject(dbm);
            objectOut.flush();
        }
        else if (msg.startsWith("reg")) {
            System.out.println("Stand by for registration");
            DBMessage dbm = new DBMessage(msgParts[0], msgParts[1], msgParts[2]);
            objectOut.writeObject(dbm);
            objectOut.flush();
        }
        else System.out.println("Unknown command");
    }

    public void setClientLogin(String clientLogin) {
        this.clientLogin = clientLogin;
    }
}
