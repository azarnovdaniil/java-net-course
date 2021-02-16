package ru.daniilazarnov;

import java.nio.file.Path;

public interface Constants {
     String DEFAULT_PATH_SERVER = Path.of("project", "server", "cloud_storage").toString();
     String DEFAULT_PATH_USER = Path.of("project", "client", "local_storage").toString();
    int TEN = 10;
    int FOUR = 4;
    int EIGHTS = 8;
    int HUNDRED = 100;
    int ONE_HUNDRED_AND_FIVE = 100;
    int TWENTY = 20;

    void nothing();
}
