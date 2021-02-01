package ru.daniilazarnov;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import ru.daniilazarnov.data.CommandData;
import ru.daniilazarnov.data.CommonData;
import ru.daniilazarnov.data.FileData;
import ru.daniilazarnov.data.TypeMessages;

import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Logger;

public class DataHandler {

    private static final Logger logger = Logger.getLogger(DataHandler.class.getName());

    private ArrayList<? super CommonData> сommandAccepted = new ArrayList<>();
    private Gson gson = new Gson();

    public StringBuffer metaData = new StringBuffer();
    public int sizeMetaData = -1;

    public DataHandler () {

    }


    public void addCommand (JsonObject jsonObject) {
        сommandAccepted.clear();

        if (jsonObject.get("type").getAsString().equals(TypeMessages.FILE.toString())) {
            this.сommandAccepted.add((FileData) gson.fromJson(this.metaData.toString(), FileData.class));
        } else if (jsonObject.get("type").getAsString().equals(TypeMessages.COMMAND.toString())) {
            this.сommandAccepted.add((CommandData) gson.fromJson(this.metaData.toString(), CommandData.class));
        }

        this.sizeMetaData = -1;
        this.metaData.delete(0, this.metaData.length());
    }

    public CommonData getCommand () {
        if (this.сommandAccepted.size() == 0) return null;
        return (CommonData) this.сommandAccepted.get(this.сommandAccepted.size()-1);
    }

}
