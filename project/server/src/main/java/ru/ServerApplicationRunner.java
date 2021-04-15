package ru;

import ru.kgogolev.*;
import ru.kgogolev.network.Server;

public class ServerApplicationRunner {
    public static void main(String[] args) {
        new ServerApplication(
                new Server(User.getDefaultUser(
                        WorkingDirectory.SERVER_WORKING_DIRECTORY,
                        WorkingDirectory.SERVER_WORKING_DIRECTORY,
                        StringConstants.DEFAULT_NAME,
                        AccessRights.NOT_AUTHORIZED)),
                new FileSystem());
    }
}
