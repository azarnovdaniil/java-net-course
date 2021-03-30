package ru.daniilazarnov.datamodel;

import ru.daniilazarnov.Pack;
import ru.daniilazarnov.PackType;

public class AuthorisationMsgPackRq extends Pack {
    private String message;

    public AuthorisationMsgPackRq() {
        super.setPackType(PackType.TEST_RES);
    }
    public String getMessage() {
        return message;
    }
}
