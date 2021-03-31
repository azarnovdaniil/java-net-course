package ru.daniilazarnov.serverOperations;

import java.io.IOException;

public interface ServerOperation {
    boolean apply() throws IOException;
}
