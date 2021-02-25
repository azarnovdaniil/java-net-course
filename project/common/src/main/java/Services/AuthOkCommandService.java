package common.Services;

import java.io.Serializable;

public class AuthOkCommandService implements Serializable
{
    private final String username;

    public AuthOkCommandService(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
