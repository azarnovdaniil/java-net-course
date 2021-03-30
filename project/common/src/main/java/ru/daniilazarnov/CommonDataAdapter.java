package ru.daniilazarnov;

import ru.daniilazarnov.datamodel.RequestDataFile;
import ru.daniilazarnov.datamodel.ResponseDataFile;

public class CommonDataAdapter {
    private int id;

    public void setId(int id) {
        this.id = id;
    }

    public Pack getPack(int id) {
        switch (id) {
            case 1:
                return new RequestDataFile();
            case 2:
                return new ResponseDataFile();
            default:
                return new RequestDataFile();
        }
    }

    public Pack getPack() {
        switch (id) {
            case 1:
                return new RequestDataFile();
            case 2:
                return new ResponseDataFile();
            default:
                return new RequestDataFile();
        }
    }
}