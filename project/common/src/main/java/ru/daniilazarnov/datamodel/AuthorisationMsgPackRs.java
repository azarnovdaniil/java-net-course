package ru.daniilazarnov.datamodel;

import ru.daniilazarnov.Pack;
import ru.daniilazarnov.PackType;

public class AuthorisationMsgPackRs extends Pack {
    String message;

    public AuthorisationMsgPackRs() {
        this.packType = PackType.AUTH_RES;
    }
}