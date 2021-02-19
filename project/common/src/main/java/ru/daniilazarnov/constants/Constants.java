package ru.daniilazarnov.constants;

import java.nio.file.Path;

public final class Constants {
    public static final String DEFAULT_PATH_SERVER = Path.of("project", "server", "cloud_storage").toString();
    public static final String DEFAULT_PATH_USER = Path.of("project", "client", "local_storage").toString();


    private Constants() {
    }
}
