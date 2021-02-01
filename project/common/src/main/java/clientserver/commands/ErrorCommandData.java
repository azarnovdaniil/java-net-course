package clientserver.commands;

import java.io.Serializable;

public class ErrorCommandData implements Serializable {

    private static final long serialVersionUID = -27784L;
    private final String errorMessage;

    public ErrorCommandData(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
