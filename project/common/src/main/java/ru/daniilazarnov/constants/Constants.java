package ru.daniilazarnov.constants;

import java.nio.file.Path;

public final class Constants {
    public static final String DEFAULT_PATH_SERVER = Path.of("project", "server", "cloud_storage").toString();
    public static final String DEFAULT_PATH_USER = Path.of("project", "client", "local_storage").toString();
    public static final int TEN = 10;
    public static final int FOUR = 4;
    public static final int FIVE = 5;
    public static final int EIGHTS = 8;
    public static final int SEVEN = 7;
    public static final int HUNDRED = 100;
    public static final int ONE_HUNDRED_AND_FIVE = 100;
    public static final int TWENTY = 20;

    private Constants() {
    }
}
