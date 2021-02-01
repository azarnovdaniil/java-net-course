package clientserver.commands;

import java.io.Serializable;

public class ChangeLoginOKCommandData implements Serializable {

    private static final long serialVersionUID = -115L;
    private final String newLogin;

    public ChangeLoginOKCommandData(String newLogin) {
        this.newLogin = newLogin;
    }

    public String getNewLogin() {
        return newLogin;
    }
}
