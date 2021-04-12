package ru.kgogolev;

import ru.kgogolev.network.Client;

public class ClientApplicationRunner {
    public static void main(String[] args) {

        new Application(new Client(
                User.getDefaultUser(
                        WorkingDirectory.CLIENT_DOWNLOAD_DIRECTORY,
                        WorkingDirectory.CLIENT_WATCH_DIRECTORY,
                        StringConstants.DEFAULT_NAME,
                        AccessRights.NOT_AUTHORIZED)));

    }
}
