package ru.daniilazarnov.datamodel;

import ru.daniilazarnov.Pack;
import ru.daniilazarnov.PackType;

public class ResponseDataFile extends Pack {
    public ResponseDataFile() {
        super.setPackType(PackType.TEST_RES);
    }

    private int intValue;

    public int getIntValue() {
        return intValue;
    }

    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }
    // standard getters and setters
}
