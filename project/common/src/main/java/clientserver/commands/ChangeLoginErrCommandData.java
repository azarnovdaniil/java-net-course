package clientserver.commands;

import java.io.Serializable;

public class ChangeLoginErrCommandData implements Serializable {

    private static final long serialVersionUID = -12749L;
    private final String errorMessage;

    public ChangeLoginErrCommandData(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
