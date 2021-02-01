package clientserver.commands;

import java.io.Serializable;

public class AuthErrorCommandData implements Serializable {

    private static final long serialVersionUID = -120L;
    private final String errorMessage;

    public AuthErrorCommandData(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
