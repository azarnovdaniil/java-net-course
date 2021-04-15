package ru.kgogolev;

public final class User {
    private String rootDownloadDirectory;
    private String rootNavigateDirectory;
    private String nickname;
    private AccessRights rights;

    private User(String rootDownloadDirectory, String rootNavigateDirectory, String nickname, AccessRights rights) {
        this.rootDownloadDirectory = rootDownloadDirectory;
        this.rootNavigateDirectory = rootNavigateDirectory;
        this.nickname = nickname;
        this.rights = rights;
    }

    public static User getDefaultUser(String defaultDownloadRoot,
                                      String defaultNavigateRoot,
                                      String defaultName,
                                      AccessRights defaultRights) {
        return new User(defaultDownloadRoot, defaultNavigateRoot, defaultName, defaultRights);
    }

    public void setRootDownloadDirectory(String rootDownloadDirectory) {
        this.rootDownloadDirectory = rootDownloadDirectory;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setRights(AccessRights rights) {
        this.rights = rights;
    }

    public String getRootDownloadDirectory() {
        return rootDownloadDirectory;
    }

    public String getNickname() {
        return nickname;
    }

    public AccessRights getRights() {
        return rights;
    }

    public String getRootNavigateDirectory() {
        return rootNavigateDirectory;
    }

    public void setRootNavigateDirectory(String rootNavigateDirectory) {
        this.rootNavigateDirectory = rootNavigateDirectory;
    }
}
