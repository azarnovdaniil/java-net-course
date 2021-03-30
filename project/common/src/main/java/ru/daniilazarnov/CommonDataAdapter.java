package ru.daniilazarnov;

import ru.daniilazarnov.datamodel.RequestDataFile;
import ru.daniilazarnov.datamodel.ResponseDataFile;

public class CommonDataAdapter {
    private PackType pt;

    public void setId(PackType packType) {
        this.pt = packType;
    }

    public Pack getPack(PackType packType) {
        switch (packType) {
            case DOWNLOAD_FILE_REQ:
                return new RequestDataFile();
            case DOWNLOAD_FILE_RES:
                return new ResponseDataFile();
            default:
                return new RequestDataFile();
        }
    }

    public Pack getPack() {
        switch (pt) {
            case DOWNLOAD_FILE_REQ:
                return new RequestDataFile();
            case DOWNLOAD_FILE_RES:
                return new ResponseDataFile();
            default:
                return new RequestDataFile();
        }
    }
}
