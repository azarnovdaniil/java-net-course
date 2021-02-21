package ru.atoroschin;

import java.io.IOException;

public interface ReadCredentials {
    Credentials read() throws IOException;

    void write(Credentials credentials) throws IOException;
}
