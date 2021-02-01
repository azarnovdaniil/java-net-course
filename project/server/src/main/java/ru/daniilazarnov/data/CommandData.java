package ru.daniilazarnov.data;

import ru.daniilazarnov.StorageServerHandler;

import java.io.Serializable;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

public class CommandData extends CommonData implements iData {
    private ArrayList<String> echo = new ArrayList<String>();
    private int command = 0;
    private int completed = 0;

    public CommandData() {
        super(TypeMessages.COMMAND);
    }

    public void addEcho (String text) {
        this.echo.add(text);
    }

    public void run (StorageServerHandler storageSH, SocketChannel ch) {
        if (this.completed == 1) return;
        CommandData cD = new CommandData();

        if (this.command == 1) {
            cD.writeInHelp();
        }

        storageSH.handleWrite(cD,ch);
        this.completed = 1;
    }

    public void notAuthorized(StorageServerHandler storageSH, SocketChannel ch) {
        if (this.completed == 1) return;
        CommandData cD = new CommandData();
        cD.addEcho("Please log in, otherwise session kill 120 sec.");
        storageSH.handleWrite(cD,ch);
        this.completed = 1;
    }

    public int getCommand() {
        return this.command;
    }

    private void writeInHelp () {
        this.echo.add("[-h]: Show help list");
        this.echo.add("[reg -lLogin -pPaswoord]: User registration");
        this.echo.add("[login -lLogin -pPaswoord]: User authorization");
        this.echo.add("[ls]: show folder");
        this.echo.add("[mkdir -p NAME]: create folder");
        this.echo.add("[cd -p URL]: transition to url");
        this.echo.add("[upload -p from -p to]: upload file to Storage Server, (*from): absolute path. (upload /Users/r.shafikov/Downloads/bmw_x3m_2021.jpeg /photo/x3m1.jpg)");
        this.echo.add("[download -p From -p To]: download file to Storage Server (*to): absolute path.");
    }









}
