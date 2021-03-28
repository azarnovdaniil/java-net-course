package ru.daniilazarnov.datamodel;

import ru.daniilazarnov.Pack;

public class RequestData extends Pack {
    //сообщение от клиента с данными
    private String stringValue;

    public int getId() {
        return super.getId();
    }

    public void setId(int id) {
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }
}
