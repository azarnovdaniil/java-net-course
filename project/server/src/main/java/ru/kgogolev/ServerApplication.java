package ru.kgogolev;

import ru.kgogolev.network.Server;

public class ServerApplication {
    private Server server;
    private FileSystem fileSystem;

    public ServerApplication(Server server, FileSystem fileSystem) {
        this.server = server;
        this.fileSystem = fileSystem;
    }
}
