package common.Services;

import java.io.Serializable;

public class AuthErrorCommandService implements Serializable
{
    private final String errorMessage;

    public AuthErrorCommandService(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
