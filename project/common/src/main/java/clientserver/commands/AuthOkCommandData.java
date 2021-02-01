package clientserver.commands;

import java.io.Serializable;

public class AuthOkCommandData implements Serializable {

    private static final long serialVersionUID = -48L;
    private final String username;

    public AuthOkCommandData(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
