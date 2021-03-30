package ru.daniilazarnov.datamodel;

import ru.daniilazarnov.Pack;
import ru.daniilazarnov.PackType;

public class AuthorisationMsgPackRq extends Pack {
    String message;

    public AuthorisationMsgPackRq() {
        this.packType = PackType.AUTH_REQ;
    }
}