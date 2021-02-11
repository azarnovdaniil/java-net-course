package com.geekbrains.dbox.server.comands;
public enum Separator {
    stringSep("#8479#");

    private String code;
    Separator(String code){
        this.code =  code;
    }


    public  String getCode(){return code;}
}
