package ru.daniilazarnov;

import helpers.ConfigHelper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class ServerApp {
    public static void main(String[] args) throws Exception {
        new Server().run();
    }
}
