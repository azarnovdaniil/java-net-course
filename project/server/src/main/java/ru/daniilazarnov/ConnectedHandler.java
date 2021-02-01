package ru.daniilazarnov;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ru.daniilazarnov.data.CommandData;
import ru.daniilazarnov.data.CommonData;
import ru.daniilazarnov.data.FileData;
import ru.daniilazarnov.data.TypeMessages;

import java.io.*;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;
import java.util.logging.Logger;

public class ConnectedHandler {

    private static final Logger logger = Logger.getLogger(ConnectedHandler.class.getName());

    public final UUID id;
    private boolean isAuthorized = true;

    private String currentLocation = "";
    private Path tempPath;

    private ArrayList<? super CommonData> сommandAccepted = new ArrayList<>();
    private SocketChannel sh;
    private StorageServerHandler ssh;
    private Gson gson = new Gson();


    public ConnectedHandler (StorageServerHandler ssh) {
        this.ssh = ssh;
        this.id = UUID.randomUUID();
    }

    public String getId () {
        return this.id.toString();
    }

    public Boolean isAuthorized () {
        return this.isAuthorized;
    }

    public void setSocketChannel (SocketChannel sh) {
        this.sh = sh;
    }

    public void addCommand (byte[] metaByte) {
        JsonObject jsonObject = JsonParser.parseString(new String(metaByte)).getAsJsonObject();
        String stringJson = new String(metaByte);

        if (jsonObject.get("type").getAsString().equals(TypeMessages.FILE.toString())) {
            this.сommandAccepted.add((FileData) gson.fromJson(stringJson, FileData.class));
        } else if (jsonObject.get("type").getAsString().equals(TypeMessages.COMMAND.toString())) {
            this.сommandAccepted.add((CommandData) gson.fromJson(stringJson, CommandData.class));
        }

        this.commandHandler(metaByte.length + StorageServerHandler.SIZE_META);

    }


    public void createTempFile () {
        this.tempPath = Paths.get(StorageServer.LOCATION_TEMP_FILES + UUID.randomUUID().toString());
        File pathFile = new File(tempPath.toString());

        try {
            pathFile.createNewFile();
        } catch (IOException e) {
            this.createTempFile();
        }
    }

    public Boolean removeTempFile () {
        return new File(tempPath.toString()).delete();
    }

    public String getTempFile () {
        return tempPath.toString();
    }

    private void commandHandler (int skipByteTempFile) {
        if (this.сommandAccepted.size() == 0) return;

        CommonData command = (CommonData) this.сommandAccepted.get(this.сommandAccepted.size()-1);

        if (!this.isAuthorized() && command.getType() == TypeMessages.AUTH) {
            System.out.println("isAuthorized");
        }

        if (!this.isAuthorized() && command.getType() == TypeMessages.COMMAND) {
            if (((CommandData)command).getCommand() == 1) {
                ((CommandData)command).run(this.ssh, this.sh);
            }
        }

        if (!this.isAuthorized() && command.getType() != TypeMessages.AUTH) {
            ((CommandData)command).notAuthorized(this.ssh, this.sh);
            return;
        }

        if (this.isAuthorized() && command.getType() == TypeMessages.COMMAND) {
            if (((CommandData)command).getCommand() == 1) {
                ((CommandData)command).run(this.ssh, this.sh);
            }
        }

        if (this.isAuthorized() && command.getType() == TypeMessages.FILE) {
            FileData fileData = (FileData) command;
            String path = StorageUtil.pathFileStorage(fileData, this.id.toString());

            try {
                FileInputStream fRead = new FileInputStream(this.getTempFile());
                FileOutputStream fWrite = new FileOutputStream(path, false);

                byte[] byteF = new byte[(int) fileData.getLengthByte()];

                fRead.skip((long) skipByteTempFile);
                fRead.read(byteF, 0, byteF.length);
                fWrite.write(byteF);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}
