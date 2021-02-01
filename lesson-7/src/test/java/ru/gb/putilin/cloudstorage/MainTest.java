package ru.gb.putilin.cloudstorage;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    void testClassNotFoundException() {
        ClassLoader classLoader = new MainTest().getClass().getClassLoader();
        assertThrows(ClassNotFoundException.class, () -> classLoader.loadClass("ru.daniilazarnov.GeekBrains"));
    }
}
