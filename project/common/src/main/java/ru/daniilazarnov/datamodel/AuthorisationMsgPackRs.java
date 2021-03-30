package ru.daniilazarnov.datamodel;

import ru.daniilazarnov.Pack;
import ru.daniilazarnov.PackType;

public class AuthorisationMsgPackRs extends Pack {
    private String message;

    public AuthorisationMsgPackRs() {
        this.packType = PackType.AUTH_RES;
    }

    public String getMessage() {
        return message;
    }
}