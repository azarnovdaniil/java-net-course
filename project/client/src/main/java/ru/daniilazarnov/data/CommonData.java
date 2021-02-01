package ru.daniilazarnov.data;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

import com.google.gson.*;

public class CommonData implements iData, Serializable {

    private TypeMessages type;

    public CommonData (TypeMessages type) {
        this.type = type;
    }

    @Override
    public TypeMessages getType() {
        return this.type;
    }

    public String jsonToString () {
        Gson gson = new Gson();
        return gson.toJson(this);
    }


    public static CommonData serialization (String[] command) {
        if (command[0].equals("-h")) {
            return new CommandData(1);
        } else if (command[0].equals("ls")) {
            return new CommandData(2);
        } else if (command[0].equals("upload")) {
            if (command.length != 3) return null;

            FileData objectData = new FileData(TypeMessages.FILE);
            System.out.println(Arrays.toString(command));
            objectData.addFile(Paths.get(command[1]));
            objectData.setCalalog(command[2]);
            return objectData;
        }


        return null;
    }

}
