package ru.daniilazarnov;

import org.junit.jupiter.api.Test;

public class TestClient {

    @Test
    void TestSplit() {
        String array = "~com Bob Sam";

        String[] parts = array.split(" ");
        String name = parts[1];
        String newName = parts[2];

        System.out.printf("%s%n%s", name, newName);
    }
}