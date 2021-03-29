package ru.daniilazarnov;

import java.util.HashMap;

public class MessageHandler {
    public String message;
    public int code;

    public MessageHandler(String command) {
        HashMap<String, Integer> commandMap = new HashMap<String, Integer>() {{
            put("-exit", 0);
            put("-help", -2);
            put("-upload", 1);
            put("-download", 2);
            put("-show", 3);
            put("-remove", 4);
            put("-rename", 5);
        }};

        try {
            this.code = commandMap.get(command.trim().split(" ")[0]);
            this.message = command;
        } catch (NullPointerException e) {
            this.code = -1;
            System.out.printf("Command %s is not supported.\n", command);
        }

        if (this.code == -2) {
            System.out.println("The following commands are supported:");
            System.out.println("-exit       Disconnect and exit.");
            System.out.println("-help       Show help.");
        }
    }
}
