package messages;

import ru.daniilazarnov.CredentialsEntry;

public class FileMessage {

    private FileOperationType type;
    private CredentialsEntry user;
    private String path1;
    private String path2;

    public FileMessage(FileOperationType type, CredentialsEntry user, String path1, String path2) {
        this.type = type;
        this.user = user;
        this.path1 = path1;
        this.path2 = path2;
    }

    public FileOperationType getType() {
        return type;
    }

    public void setType(FileOperationType type) {
        this.type = type;
    }

    public CredentialsEntry getUser() {
        return user;
    }

    public void setUser(CredentialsEntry user) {
        this.user = user;
    }

    public String getPath1() {
        return path1;
    }

    public void setPath1(String path1) {
        this.path1 = path1;
    }

    public String getPath2() {
        return path2;
    }

    public void setPath2(String path2) {
        this.path2 = path2;
    }
}
