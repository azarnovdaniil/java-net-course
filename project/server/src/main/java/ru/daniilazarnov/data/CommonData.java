package ru.daniilazarnov.data;

import com.google.gson.Gson;

import java.io.*;

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

}
