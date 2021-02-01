package ru.daniilazarnov.data;

import java.io.Serializable;
import java.util.ArrayList;

public class CommandData extends CommonData implements iData {
    private ArrayList<String> echo = new ArrayList<String>();
    private int command = 0;
    private int completed = 0;

    public CommandData() {
        super(TypeMessages.COMMAND);
    }

    public CommandData(int command) {
        super(TypeMessages.COMMAND);
        this.command = command;
    }

    public void run () {
        if (this.completed == 1) return;
        this.showEcho();
        this.completed = 1;
    }

    private void showEcho () {
        if (this.echo.size() == 0) return;
        this.echo.forEach((n) -> System.out.println(n));
    }

}
