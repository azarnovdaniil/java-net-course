package com.geekbrains.dbox.server.comands;

public enum Comand {
    ls(1), createDir(2), delete(3), rename(4), Auth(5);

    private byte code;
    Comand(int code){
        this.code = (byte) code;
    }


    public  byte getCode(){return code;}
}

