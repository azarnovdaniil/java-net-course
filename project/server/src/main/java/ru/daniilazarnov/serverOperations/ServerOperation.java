package ru.daniilazarnov.serverOperations;

import java.io.IOException;
import java.nio.channels.SelectionKey;

public interface ServerOperation {

    boolean apply() throws IOException;
}
