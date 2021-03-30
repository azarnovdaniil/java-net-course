package ru.daniilazarnov.datamodel;

import ru.daniilazarnov.Pack;
import ru.daniilazarnov.PackType;

public class AuthorisationMsgPackRq extends Pack {
    private String message;

    public AuthorisationMsgPackRq() {
        this.packType = PackType.AUTH_REQ;
    }

    public String getMessage() {
        return message;
    }
}